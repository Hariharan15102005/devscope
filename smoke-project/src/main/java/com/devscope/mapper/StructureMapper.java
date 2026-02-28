package com.devscope.mapper;

import com.devscope.dto.response.ClassDetailResponse;
import com.devscope.dto.response.StructureTreeResponse;
import com.devscope.model.JavaClassEntity;
import com.devscope.model.MetricEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class StructureMapper {

    public StructureTreeResponse toTreeResponse(List<JavaClassEntity> entities) {
        Map<String, List<StructureTreeResponse.ClassNode>> grouped = new LinkedHashMap<>();
        for (JavaClassEntity entity : entities) {
            grouped.computeIfAbsent(entity.getPackageName(), ignored -> new ArrayList<>())
                    .add(new StructureTreeResponse.ClassNode(entity.getFullName(), entity.getClassName(), entity.getAnnotationType()));
        }

        StructureTreeResponse response = new StructureTreeResponse();
        response.setPackages(grouped.entrySet().stream()
                .map(entry -> new StructureTreeResponse.PackageNode(entry.getKey(), entry.getValue()))
                .toList());
        return response;
    }

    public ClassDetailResponse toClassDetail(JavaClassEntity entity, MetricEntity metricEntity) {
        ClassDetailResponse response = new ClassDetailResponse();
        response.setFullName(entity.getFullName());
        response.setClassName(entity.getClassName());
        response.setPackageName(entity.getPackageName());
        response.setAnnotation(entity.getAnnotationType());
        response.setRiskScore(toRiskScore(metricEntity == null ? null : metricEntity.getRiskLevel()));
        response.setMethodCount(metricEntity == null ? 0 : metricEntity.getMethodCount());
        response.setDependencyCount(metricEntity == null ? 0 : metricEntity.getDependencyCount());
        response.setDescription("Structure metadata mapped from analysis entities.");
        return response;
    }

    private int toRiskScore(String riskLevel) {
        if ("HIGH".equalsIgnoreCase(riskLevel)) {
            return 90;
        }
        if ("MEDIUM".equalsIgnoreCase(riskLevel)) {
            return 65;
        }
        return 40;
    }
}
