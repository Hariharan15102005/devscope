package com.devscope.service;

import com.devscope.dto.response.ClassDetailResponse;
import com.devscope.dto.response.StructureTreeResponse;
import com.devscope.exception.ProjectNotFoundException;
import com.devscope.mapper.StructureMapper;
import com.devscope.model.MetricEntity;
import com.devscope.repository.JavaClassRepository;
import com.devscope.repository.MetricRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class StructureService {

    private final JavaClassRepository javaClassRepository;
    private final MetricRepository metricRepository;
    private final StructureMapper structureMapper;

    public StructureService(
            JavaClassRepository javaClassRepository,
            MetricRepository metricRepository,
            StructureMapper structureMapper
    ) {
        this.javaClassRepository = javaClassRepository;
        this.metricRepository = metricRepository;
        this.structureMapper = structureMapper;
    }

    public StructureTreeResponse getStructureTree(Long analysisId, String filterType) {
        List<com.devscope.model.JavaClassEntity> classes = javaClassRepository.findByAnalysisRun_Id(analysisId);
        if (classes.isEmpty()) {
            throw new ProjectNotFoundException(String.valueOf(analysisId));
        }

        String normalizedFilter = normalize(filterType);
        if (normalizedFilter != null) {
            classes = classes.stream()
                    .filter(item -> normalize(item.getAnnotationType()).equals(normalizedFilter))
                    .toList();
        }

        return structureMapper.toTreeResponse(classes);
    }

    public ClassDetailResponse getClassDetail(Long analysisId, String className) {
        if (className == null || className.isBlank()) {
            return null;
        }

        var entity = javaClassRepository.findByAnalysisRun_IdAndFullName(analysisId, className)
                .or(() -> javaClassRepository.findByAnalysisRun_Id(analysisId).stream()
                        .filter(item -> item.getClassName().equals(className))
                        .findFirst());

        if (entity.isEmpty()) {
            return null;
        }

        MetricEntity metric = metricRepository.findByAnalysisRun_Id(analysisId).stream()
                .filter(item -> item.getClassName().equals(entity.get().getFullName()))
                .findFirst()
                .orElse(null);

        return structureMapper.toClassDetail(entity.get(), metric);
    }

    private static String normalize(String value) {
        return value == null ? null : value.trim().toLowerCase(Locale.ROOT);
    }
}
