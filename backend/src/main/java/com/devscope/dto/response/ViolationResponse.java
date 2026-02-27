package com.devscope.dto.response;

import java.util.List;

public class ViolationResponse {
    private String id;
    private String type;
    private String severity;
    private String title;
    private String description;
    private List<String> affectedClasses;
    private String recommendation;

    public ViolationResponse() {
    }

    public ViolationResponse(
            String id,
            String type,
            String severity,
            String title,
            String description,
            List<String> affectedClasses,
            String recommendation
    ) {
        this.id = id;
        this.type = type;
        this.severity = severity;
        this.title = title;
        this.description = description;
        this.affectedClasses = affectedClasses;
        this.recommendation = recommendation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
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

    public List<String> getAffectedClasses() {
        return affectedClasses;
    }

    public void setAffectedClasses(List<String> affectedClasses) {
        this.affectedClasses = affectedClasses;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }
}
