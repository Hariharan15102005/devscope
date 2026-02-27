package com.devscope.mapper;

import com.devscope.dto.response.ViolationResponse;
import com.devscope.model.ViolationEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ViolationMapper {

    public ViolationResponse toResponse(ViolationEntity entity) {
        ViolationResponse response = new ViolationResponse();
        response.setId(entity.getCode());
        response.setType(entity.getType());
        response.setSeverity(entity.getSeverity());
        response.setTitle(entity.getTitle());
        response.setDescription(entity.getDescription());
        response.setRecommendation(entity.getRecommendation());
        response.setAffectedClasses(csvToList(entity.getAffectedClassesCsv()));
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
