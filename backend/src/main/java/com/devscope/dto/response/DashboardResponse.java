package com.devscope.dto.response;

import java.util.List;
import java.util.Map;

public class DashboardResponse {
    private int totalPackages;
    private int totalClasses;
    private int totalFiles;
    private int totalDependencies;
    private double healthScore;
    private Map<String, Integer> layerDistribution;
    private Map<String, Integer> complexityDistribution;
    private List<RiskyClassItem> topRiskyClasses;
    private List<ViolationItem> topViolations;
    private int cyclicDependenciesCount;

    public int getTotalPackages() {
        return totalPackages;
    }

    public void setTotalPackages(int totalPackages) {
        this.totalPackages = totalPackages;
    }

    public int getTotalClasses() {
        return totalClasses;
    }

    public void setTotalClasses(int totalClasses) {
        this.totalClasses = totalClasses;
    }

    public int getTotalFiles() {
        return totalFiles;
    }

    public void setTotalFiles(int totalFiles) {
        this.totalFiles = totalFiles;
    }

    public int getTotalDependencies() {
        return totalDependencies;
    }

    public void setTotalDependencies(int totalDependencies) {
        this.totalDependencies = totalDependencies;
    }

    public double getHealthScore() {
        return healthScore;
    }

    public void setHealthScore(double healthScore) {
        this.healthScore = healthScore;
    }

    public Map<String, Integer> getLayerDistribution() {
        return layerDistribution;
    }

    public void setLayerDistribution(Map<String, Integer> layerDistribution) {
        this.layerDistribution = layerDistribution;
    }

    public Map<String, Integer> getComplexityDistribution() {
        return complexityDistribution;
    }

    public void setComplexityDistribution(Map<String, Integer> complexityDistribution) {
        this.complexityDistribution = complexityDistribution;
    }

    public List<RiskyClassItem> getTopRiskyClasses() {
        return topRiskyClasses;
    }

    public void setTopRiskyClasses(List<RiskyClassItem> topRiskyClasses) {
        this.topRiskyClasses = topRiskyClasses;
    }

    public List<ViolationItem> getTopViolations() {
        return topViolations;
    }

    public void setTopViolations(List<ViolationItem> topViolations) {
        this.topViolations = topViolations;
    }

    public int getCyclicDependenciesCount() {
        return cyclicDependenciesCount;
    }

    public void setCyclicDependenciesCount(int cyclicDependenciesCount) {
        this.cyclicDependenciesCount = cyclicDependenciesCount;
    }

    public static class RiskyClassItem {
        private String className;
        private int riskScore;

        public RiskyClassItem() {
        }

        public RiskyClassItem(String className, int riskScore) {
            this.className = className;
            this.riskScore = riskScore;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public int getRiskScore() {
            return riskScore;
        }

        public void setRiskScore(int riskScore) {
            this.riskScore = riskScore;
        }
    }

    public static class ViolationItem {
        private String violationId;
        private String message;
        private int severity;

        public ViolationItem() {
        }

        public ViolationItem(String violationId, String message, int severity) {
            this.violationId = violationId;
            this.message = message;
            this.severity = severity;
        }

        public String getViolationId() {
            return violationId;
        }

        public void setViolationId(String violationId) {
            this.violationId = violationId;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getSeverity() {
            return severity;
        }

        public void setSeverity(int severity) {
            this.severity = severity;
        }
    }
}
