package com.devscope.service;

import com.devscope.engine.graph.DependencyGraph;
import com.devscope.engine.graph.DependencyGraphBuilder;
import com.devscope.engine.graph.GraphAnalyzer;
import com.devscope.engine.insight.InsightGenerator;
import com.devscope.engine.metrics.MetricSnapshot;
import com.devscope.engine.metrics.MetricsCalculator;
import com.devscope.engine.parser.JavaParserEngine;
import com.devscope.engine.parser.ParsedJavaClass;
import com.devscope.engine.rulesengine.RuleContext;
import com.devscope.engine.rulesengine.RuleEngine;
import com.devscope.engine.rulesengine.RuleResult;
import com.devscope.engine.scanner.ProjectScanner;
import com.devscope.exception.AnalysisException;
import com.devscope.model.AnalysisRun;
import com.devscope.model.DependencyEntity;
import com.devscope.model.InsightEntity;
import com.devscope.model.JavaClassEntity;
import com.devscope.model.MetricEntity;
import com.devscope.model.Project;
import com.devscope.model.ViolationEntity;
import com.devscope.repository.AnalysisRunRepository;
import com.devscope.repository.DependencyRepository;
import com.devscope.repository.InsightRepository;
import com.devscope.repository.JavaClassRepository;
import com.devscope.repository.MetricRepository;
import com.devscope.repository.ProjectRepository;
import com.devscope.repository.ViolationRepository;
import com.devscope.util.ZipExtractor;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Service
public class AnalysisService {

    private static final Logger log = LoggerFactory.getLogger(AnalysisService.class);

    private final ProjectScanner projectScanner;
    private final JavaParserEngine javaParserEngine;
    private final DependencyGraphBuilder dependencyGraphBuilder;
    private final GraphAnalyzer graphAnalyzer;
    private final MetricsCalculator metricsCalculator;
    private final RuleEngine ruleEngine;
    private final InsightGenerator insightGenerator;
    private final ProjectRepository projectRepository;
    private final AnalysisRunRepository analysisRunRepository;
    private final JavaClassRepository javaClassRepository;
    private final DependencyRepository dependencyRepository;
    private final MetricRepository metricRepository;
    private final ViolationRepository violationRepository;
    private final InsightRepository insightRepository;

    public AnalysisService(
            ProjectScanner projectScanner,
            JavaParserEngine javaParserEngine,
            DependencyGraphBuilder dependencyGraphBuilder,
            GraphAnalyzer graphAnalyzer,
            MetricsCalculator metricsCalculator,
            RuleEngine ruleEngine,
            InsightGenerator insightGenerator,
            ProjectRepository projectRepository,
            AnalysisRunRepository analysisRunRepository,
            JavaClassRepository javaClassRepository,
            DependencyRepository dependencyRepository,
            MetricRepository metricRepository,
            ViolationRepository violationRepository,
            InsightRepository insightRepository
    ) {
        this.projectScanner = projectScanner;
        this.javaParserEngine = javaParserEngine;
        this.dependencyGraphBuilder = dependencyGraphBuilder;
        this.graphAnalyzer = graphAnalyzer;
        this.metricsCalculator = metricsCalculator;
        this.ruleEngine = ruleEngine;
        this.insightGenerator = insightGenerator;
        this.projectRepository = projectRepository;
        this.analysisRunRepository = analysisRunRepository;
        this.javaClassRepository = javaClassRepository;
        this.dependencyRepository = dependencyRepository;
        this.metricRepository = metricRepository;
        this.violationRepository = violationRepository;
        this.insightRepository = insightRepository;
    }

    @Transactional
    public Long processUpload(MultipartFile file) {
        Path tempZip = null;
        Path extractedRoot = null;
        try {
            tempZip = Files.createTempFile("devscope-upload-", ".zip");
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, tempZip, StandardCopyOption.REPLACE_EXISTING);
            }

            extractedRoot = ZipExtractor.extractToTemp(tempZip);
            List<Path> javaFiles = projectScanner.scanProject(extractedRoot);
            // Log discovered files for debugging upload issues
            log.info("Extracted project at: {}. Java files found: {}", extractedRoot, javaFiles.size());
            if (!javaFiles.isEmpty()) {
                javaFiles.stream().limit(20).forEach(p -> log.debug("Java file: {}", p));
            }
            if (javaFiles.isEmpty()) {
                // Collect a short listing of extracted files to aid debugging client uploads
                try (var stream = Files.walk(extractedRoot)) {
                    var sample = stream
                            .filter(Files::isRegularFile)
                            .map(p -> extractedRoot.relativize(p).toString())
                            .limit(50)
                            .toList();
                    log.warn("No Java files found in uploaded archive. Sample contents: {}", sample);
                    throw new AnalysisException("No Java source files found inside uploaded ZIP; sample contents: " + sample);
                } catch (IOException ioe) {
                    log.warn("No Java files and failed to list extracted contents", ioe);
                    throw new AnalysisException("No Java source files found inside uploaded ZIP");
                }
            }

            List<ParsedJavaClass> parsedClasses = javaParserEngine.parseFiles(extractedRoot, javaFiles);
            if (parsedClasses.isEmpty()) {
                throw new AnalysisException("Unable to parse Java source files from uploaded ZIP");
            }

            DependencyGraph dependencyGraph = dependencyGraphBuilder.buildGraph(parsedClasses);
            List<List<String>> cycles = graphAnalyzer.detectCycles(dependencyGraph);
            Map<String, MetricSnapshot> metricsByClass = metricsCalculator.calculateMetrics(parsedClasses, dependencyGraph);
            RuleContext ruleContext = new RuleContext(parsedClasses, dependencyGraph, metricsByClass);
            RuleResult ruleResult = ruleEngine.execute(ruleContext);
            List<InsightGenerator.InsightRecommendation> insights = insightGenerator.generateInsights(ruleResult, metricsByClass);

            Project project = resolveProject(file.getOriginalFilename());
            AnalysisRun run = new AnalysisRun();
            run.setProject(project);
            run.setRunKey(project.getProjectKey() + "-" + Instant.now().toEpochMilli());
            run.setStatus("COMPLETED");
            run = analysisRunRepository.save(run);

            saveClasses(run, parsedClasses);
            saveDependencies(run, dependencyGraph, cycles);
            saveMetrics(run, metricsByClass);
            saveViolations(run, ruleResult);
            saveInsights(run, insights);

            return run.getId();
        } catch (IOException exception) {
            throw new AnalysisException("Failed to process uploaded project", exception);
        } finally {
            deleteIfExists(tempZip);
            deleteRecursively(extractedRoot);
        }
    }

    private Project resolveProject(String originalFilename) {
        String name = originalFilename == null ? "uploaded-project" : originalFilename.replaceAll("\\.zip$", "");
        String normalizedKey = name.trim().toLowerCase().replaceAll("[^a-z0-9-]+", "-").replaceAll("-{2,}", "-");
        if (normalizedKey.isBlank()) {
            normalizedKey = "uploaded-project";
        }

        String finalKey = normalizedKey;
        return projectRepository.findByProjectKey(finalKey).orElseGet(() -> {
            Project project = new Project();
            project.setProjectKey(finalKey);
            project.setName(name.isBlank() ? "Uploaded Project" : name);
            return projectRepository.save(project);
        });
    }

    private void saveClasses(AnalysisRun run, List<ParsedJavaClass> parsedClasses) {
        List<JavaClassEntity> classEntities = parsedClasses.stream().map(parsed -> {
            JavaClassEntity entity = new JavaClassEntity();
            entity.setAnalysisRun(run);
            entity.setPackageName(parsed.getPackageName() == null ? "default" : parsed.getPackageName());
            entity.setClassName(parsed.getClassName());
            entity.setFullName(parsed.getFullName());
            entity.setAnnotationType(parsed.getAnnotations().isEmpty() ? "None" : parsed.getAnnotations().get(0));
            return entity;
        }).toList();
        javaClassRepository.saveAll(Objects.requireNonNull(classEntities));
    }

    private void saveDependencies(AnalysisRun run, DependencyGraph graph, List<List<String>> cycles) {
        Set<String> cycleEdges = new HashSet<>();
        for (List<String> cycle : cycles) {
            for (int index = 0; index < cycle.size() - 1; index++) {
                cycleEdges.add(cycle.get(index) + "->" + cycle.get(index + 1));
            }
        }

        List<DependencyEntity> dependencyEntities = graph.getAdjacency().entrySet().stream()
                .flatMap(entry -> entry.getValue().stream().map(target -> {
                    DependencyEntity entity = new DependencyEntity();
                    entity.setAnalysisRun(run);
                    entity.setSourceClass(entry.getKey());
                    entity.setTargetClass(target);
                    entity.setCycle(cycleEdges.contains(entry.getKey() + "->" + target));
                    return entity;
                }))
                .toList();

        dependencyRepository.saveAll(Objects.requireNonNull(dependencyEntities));
    }

    private void saveMetrics(AnalysisRun run, Map<String, MetricSnapshot> metricsByClass) {
        List<MetricEntity> metricEntities = metricsByClass.values().stream().map(snapshot -> {
            MetricEntity entity = new MetricEntity();
            entity.setAnalysisRun(run);
            entity.setClassName(snapshot.getClassName());
            entity.setLinesOfCode(snapshot.getLinesOfCode());
            entity.setMethodCount(snapshot.getMethodCount());
            entity.setDependencyCount(snapshot.getDependencyCount());
            entity.setRiskLevel(snapshot.getRiskLevel());
            return entity;
        }).toList();

        metricRepository.saveAll(Objects.requireNonNull(metricEntities));
    }

    private void saveViolations(AnalysisRun run, RuleResult ruleResult) {
        List<ViolationEntity> entities = ruleResult.getViolations().stream().map(violation -> {
            ViolationEntity entity = new ViolationEntity();
            entity.setAnalysisRun(run);
            entity.setCode(violation.getCode());
            entity.setType(violation.getType());
            entity.setSeverity(violation.getSeverity());
            entity.setTitle(violation.getTitle());
            entity.setDescription(violation.getDescription());
            entity.setRecommendation(violation.getRecommendation());
            entity.setAffectedClassesCsv(String.join(",", violation.getAffectedClasses()));
            return entity;
        }).toList();

        violationRepository.saveAll(Objects.requireNonNull(entities));
    }

    private void saveInsights(AnalysisRun run, List<InsightGenerator.InsightRecommendation> insights) {
        List<InsightEntity> entities = insights.stream().map(insight -> {
            InsightEntity entity = new InsightEntity();
            entity.setAnalysisRun(run);
            entity.setCode(insight.getId());
            entity.setTitle(insight.getTitle());
            entity.setDescription(insight.getDescription());
            entity.setImpactLevel(insight.getImpactLevel());
            entity.setCategory(insight.getCategory());
            entity.setReasoning(insight.getReasoning());
            entity.setAffectedClassesCsv(String.join(",", insight.getAffectedClasses()));
            entity.setRecommendationStepsCsv(String.join(",", insight.getRecommendationSteps()));
            return entity;
        }).toList();

        insightRepository.saveAll(Objects.requireNonNull(entities));
    }

    private void deleteIfExists(Path path) {
        if (path == null) {
            return;
        }
        try {
            Files.deleteIfExists(path);
        } catch (IOException ignored) {
        }
    }

    private void deleteRecursively(Path root) {
        if (root == null) {
            return;
        }
        try (var stream = Files.walk(root)) {
            stream.sorted((first, second) -> second.compareTo(first)).forEach(path -> {
                try {
                    Files.deleteIfExists(path);
                } catch (IOException ignored) {
                }
            });
        } catch (IOException ignored) {
        }
    }
}
