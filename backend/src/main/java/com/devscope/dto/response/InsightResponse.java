package com.devscope.dto.response;

import java.util.List;

public class InsightResponse {
    private String id;
    private String title;
    private String description;
    private String impactLevel;
    private String category;
    private List<String> affectedClasses;
    private String reasoning;
    private List<String> recommendationSteps;

    public InsightResponse() {
    }

    public InsightResponse(
            String id,
            String title,
            String description,
            String impactLevel,
            String category,
            List<String> affectedClasses,
            String reasoning,
            List<String> recommendationSteps
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.impactLevel = impactLevel;
        this.category = category;
        this.affectedClasses = affectedClasses;
        this.reasoning = reasoning;
        this.recommendationSteps = recommendationSteps;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImpactLevel() {
        return impactLevel;
    }

    public void setImpactLevel(String impactLevel) {
        this.impactLevel = impactLevel;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getAffectedClasses() {
        return affectedClasses;
    }

    public void setAffectedClasses(List<String> affectedClasses) {
        this.affectedClasses = affectedClasses;
    }

    public String getReasoning() {
        return reasoning;
    }

    public void setReasoning(String reasoning) {
        this.reasoning = reasoning;
    }

    public List<String> getRecommendationSteps() {
        return recommendationSteps;
    }

    public void setRecommendationSteps(List<String> recommendationSteps) {
        this.recommendationSteps = recommendationSteps;
    }
}
