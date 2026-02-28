package com.devscope.engine.insight;

import com.devscope.engine.metrics.MetricSnapshot;
import com.devscope.engine.rulesengine.RuleResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class InsightGenerator {

    private final PriorityEngine priorityEngine;

    public InsightGenerator(PriorityEngine priorityEngine) {
        this.priorityEngine = priorityEngine;
    }

    public List<InsightRecommendation> generateInsights(
            RuleResult ruleResult,
            Map<String, MetricSnapshot> metricsByClass
    ) {
        List<InsightRecommendation> recommendations = new ArrayList<>();

        for (RuleResult.RuleViolation violation : ruleResult.getViolations()) {
            String firstClass = violation.getAffectedClasses().isEmpty() ? null : violation.getAffectedClasses().get(0);
            MetricSnapshot metric = firstClass == null ? null : metricsByClass.get(firstClass);

            InsightRecommendation recommendation = new InsightRecommendation();
            recommendation.setId("I-" + violation.getCode());
            recommendation.setTitle("Recommendation for " + violation.getType());
            recommendation.setDescription(violation.getDescription());
            recommendation.setCategory(violation.getType());
            recommendation.setImpactLevel(priorityEngine.assignImpact(violation, metric));
            recommendation.setAffectedClasses(violation.getAffectedClasses());
            recommendation.setReasoning("Generated from rule engine result and metric context.");
            recommendation.setRecommendationSteps(List.of(violation.getRecommendation()));
            recommendations.add(recommendation);
        }

        return recommendations;
    }

    public static class InsightRecommendation {
        private String id;
        private String title;
        private String description;
        private String impactLevel;
        private String category;
        private List<String> affectedClasses = new ArrayList<>();
        private String reasoning;
        private List<String> recommendationSteps = new ArrayList<>();

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
}
