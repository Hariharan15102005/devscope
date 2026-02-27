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
import com.devscope.model.AnalysisRun;
import com.devscope.repository.AnalysisRunRepository;
import com.devscope.repository.DependencyRepository;
import com.devscope.repository.InsightRepository;
import com.devscope.repository.JavaClassRepository;
import com.devscope.repository.MetricRepository;
import com.devscope.repository.ProjectRepository;
import com.devscope.repository.ViolationRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Service
public class AnalysisPipelineService {

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

    public AnalysisPipelineService(
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

    public AnalysisRun executeAnalysis(String projectKey, Path extractedProjectRoot) throws IOException {
        List<Path> javaFiles = projectScanner.scanProject(extractedProjectRoot);
        List<ParsedJavaClass> parsedClasses = javaParserEngine.parseFiles(extractedProjectRoot, javaFiles);

        DependencyGraph dependencyGraph = dependencyGraphBuilder.buildGraph(parsedClasses);
        graphAnalyzer.detectCycles(dependencyGraph);

        Map<String, MetricSnapshot> metricsByClass = metricsCalculator.calculateMetrics(parsedClasses, dependencyGraph);
        RuleContext context = new RuleContext(parsedClasses, dependencyGraph, metricsByClass);
        RuleResult ruleResult = ruleEngine.execute(context);
        insightGenerator.generateInsights(ruleResult, metricsByClass);

        return persistAnalysisArtifacts(projectKey, parsedClasses, dependencyGraph, metricsByClass, ruleResult);
    }

    private AnalysisRun persistAnalysisArtifacts(
            String projectKey,
            List<ParsedJavaClass> parsedClasses,
            DependencyGraph dependencyGraph,
            Map<String, MetricSnapshot> metricsByClass,
            RuleResult ruleResult
    ) {
        AnalysisRun run = new AnalysisRun();
        run.setRunKey(projectKey + "-pending-run");
        run.setStatus("PENDING");

        projectRepository.count();
        analysisRunRepository.count();
        javaClassRepository.count();
        dependencyRepository.count();
        metricRepository.count();
        violationRepository.count();
        insightRepository.count();

        return run;
    }
}
