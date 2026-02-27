package com.devscope.engine.metrics;

public class MetricSnapshot {
    private String className;
    private int linesOfCode;
    private int methodCount;
    private int dependencyCount;
    private String riskLevel;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getLinesOfCode() {
        return linesOfCode;
    }

    public void setLinesOfCode(int linesOfCode) {
        this.linesOfCode = linesOfCode;
    }

    public int getMethodCount() {
        return methodCount;
    }

    public void setMethodCount(int methodCount) {
        this.methodCount = methodCount;
    }

    public int getDependencyCount() {
        return dependencyCount;
    }

    public void setDependencyCount(int dependencyCount) {
        this.dependencyCount = dependencyCount;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }
}
