package com.devscope.dto.response;

import java.util.List;
import java.util.Map;

public class DashboardResponse {
    private Long analysisId;
    private Summary summary;
    private List<RiskOverviewItem> riskOverview;
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

    public Long getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(Long analysisId) {
        this.analysisId = analysisId;
    }

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    public List<RiskOverviewItem> getRiskOverview() {
        return riskOverview;
    }

    public void setRiskOverview(List<RiskOverviewItem> riskOverview) {
        this.riskOverview = riskOverview;
    }

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

    public int getCyclicDependencyCount() {
        return cyclicDependenciesCount;
    }

    public void setCyclicDependencyCount(int cyclicDependencyCount) {
        this.cyclicDependenciesCount = cyclicDependencyCount;
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

    public static class Summary {
        private int totalClasses;
        private int totalDependencies;
        private int totalViolations;
        private int cyclicDependencyCount;
        private int healthScore;

        public int getTotalClasses() {
            return totalClasses;
        }

        public void setTotalClasses(int totalClasses) {
            this.totalClasses = totalClasses;
        }

        public int getTotalDependencies() {
            return totalDependencies;
        }

        public void setTotalDependencies(int totalDependencies) {
            this.totalDependencies = totalDependencies;
        }

        public int getTotalViolations() {
            return totalViolations;
        }

        public void setTotalViolations(int totalViolations) {
            this.totalViolations = totalViolations;
        }

        public int getCyclicDependencyCount() {
            return cyclicDependencyCount;
        }

        public void setCyclicDependencyCount(int cyclicDependencyCount) {
            this.cyclicDependencyCount = cyclicDependencyCount;
        }

        public int getHealthScore() {
            return healthScore;
        }

        public void setHealthScore(int healthScore) {
            this.healthScore = healthScore;
        }
    }

    public static class RiskOverviewItem {
        private String className;
        private String riskLevel;
        private String reason;

        public RiskOverviewItem() {
        }

        public RiskOverviewItem(String className, String riskLevel, String reason) {
            this.className = className;
            this.riskLevel = riskLevel;
            this.reason = reason;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getRiskLevel() {
            return riskLevel;
        }

        public void setRiskLevel(String riskLevel) {
            this.riskLevel = riskLevel;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
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
