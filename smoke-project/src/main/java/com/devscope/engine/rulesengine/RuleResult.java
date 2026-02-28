package com.devscope.engine.rulesengine;

import java.util.ArrayList;
import java.util.List;

public class RuleResult {
    private final List<RuleViolation> violations = new ArrayList<>();

    public List<RuleViolation> getViolations() {
        return violations;
    }

    public void addViolation(RuleViolation violation) {
        this.violations.add(violation);
    }

    public void addViolations(List<RuleViolation> incoming) {
        this.violations.addAll(incoming);
    }

    public static class RuleViolation {
        private String code;
        private String type;
        private String severity;
        private String title;
        private String description;
        private List<String> affectedClasses = new ArrayList<>();
        private String recommendation;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
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
}
