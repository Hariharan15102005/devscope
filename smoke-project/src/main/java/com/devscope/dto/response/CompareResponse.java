package com.devscope.dto.response;

import java.util.List;

public class CompareResponse {
    private Summary summary;
    private List<MetricDifference> metricDifferences;
    private ViolationDifferences violationDifferences;

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    public List<MetricDifference> getMetricDifferences() {
        return metricDifferences;
    }

    public void setMetricDifferences(List<MetricDifference> metricDifferences) {
        this.metricDifferences = metricDifferences;
    }

    public ViolationDifferences getViolationDifferences() {
        return violationDifferences;
    }

    public void setViolationDifferences(ViolationDifferences violationDifferences) {
        this.violationDifferences = violationDifferences;
    }

    public static class Summary {
        private int violationChange;
        private double avgComplexityChange;
        private int cycleChange;
        private int riskChange;
        private double complexityBefore;
        private double complexityAfter;

        public int getViolationChange() {
            return violationChange;
        }

        public void setViolationChange(int violationChange) {
            this.violationChange = violationChange;
        }

        public double getAvgComplexityChange() {
            return avgComplexityChange;
        }

        public void setAvgComplexityChange(double avgComplexityChange) {
            this.avgComplexityChange = avgComplexityChange;
        }

        public int getCycleChange() {
            return cycleChange;
        }

        public void setCycleChange(int cycleChange) {
            this.cycleChange = cycleChange;
        }

        public int getRiskChange() {
            return riskChange;
        }

        public void setRiskChange(int riskChange) {
            this.riskChange = riskChange;
        }

        public double getComplexityBefore() {
            return complexityBefore;
        }

        public void setComplexityBefore(double complexityBefore) {
            this.complexityBefore = complexityBefore;
        }

        public double getComplexityAfter() {
            return complexityAfter;
        }

        public void setComplexityAfter(double complexityAfter) {
            this.complexityAfter = complexityAfter;
        }
    }

    public static class MetricDifference {
        private String className;
        private int locDiff;
        private int dependencyDiff;
        private String riskBefore;
        private String riskAfter;

        public MetricDifference() {
        }

        public MetricDifference(String className, int locDiff, int dependencyDiff, String riskBefore, String riskAfter) {
            this.className = className;
            this.locDiff = locDiff;
            this.dependencyDiff = dependencyDiff;
            this.riskBefore = riskBefore;
            this.riskAfter = riskAfter;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public int getLocDiff() {
            return locDiff;
        }

        public void setLocDiff(int locDiff) {
            this.locDiff = locDiff;
        }

        public int getDependencyDiff() {
            return dependencyDiff;
        }

        public void setDependencyDiff(int dependencyDiff) {
            this.dependencyDiff = dependencyDiff;
        }

        public String getRiskBefore() {
            return riskBefore;
        }

        public void setRiskBefore(String riskBefore) {
            this.riskBefore = riskBefore;
        }

        public String getRiskAfter() {
            return riskAfter;
        }

        public void setRiskAfter(String riskAfter) {
            this.riskAfter = riskAfter;
        }
    }

    public static class ViolationDifferences {
        private int added;
        private int removed;

        public ViolationDifferences() {
        }

        public ViolationDifferences(int added, int removed) {
            this.added = added;
            this.removed = removed;
        }

        public int getAdded() {
            return added;
        }

        public void setAdded(int added) {
            this.added = added;
        }

        public int getRemoved() {
            return removed;
        }

        public void setRemoved(int removed) {
            this.removed = removed;
        }
    }
}
