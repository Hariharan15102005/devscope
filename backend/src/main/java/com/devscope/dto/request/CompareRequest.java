package com.devscope.dto.request;

public class CompareRequest {
    private String baseAnalysisId;
    private String targetAnalysisId;

    public String getBaseAnalysisId() {
        return baseAnalysisId;
    }

    public void setBaseAnalysisId(String baseAnalysisId) {
        this.baseAnalysisId = baseAnalysisId;
    }

    public String getTargetAnalysisId() {
        return targetAnalysisId;
    }

    public void setTargetAnalysisId(String targetAnalysisId) {
        this.targetAnalysisId = targetAnalysisId;
    }
}
