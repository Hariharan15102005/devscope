package com.devscope.mapper;

import com.devscope.dto.response.InsightResponse;
import com.devscope.model.InsightEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class InsightMapper {

    public InsightResponse toResponse(InsightEntity entity) {
        InsightResponse response = new InsightResponse();
        response.setId(entity.getCode());
        response.setTitle(entity.getTitle());
        response.setDescription(entity.getDescription());
        response.setImpactLevel(entity.getImpactLevel());
        response.setCategory(entity.getCategory());
        response.setReasoning(entity.getReasoning());
        response.setAffectedClasses(csvToList(entity.getAffectedClassesCsv()));
        response.setRecommendationSteps(csvToList(entity.getRecommendationStepsCsv()));
        return response;
    }

    private List<String> csvToList(String csv) {
        if (csv == null || csv.isBlank()) {
            return List.of();
        }
        return Arrays.stream(csv.split(","))
                .map(String::trim)
                .filter(item -> !item.isBlank())
                .toList();
    }
}
