package com.devscope.dto.request;

public class CompareRequest {
    private Long baseAnalysisId;
    private Long targetAnalysisId;

    public Long getBaseAnalysisId() {
        return baseAnalysisId;
    }

    public void setBaseAnalysisId(Long baseAnalysisId) {
        this.baseAnalysisId = baseAnalysisId;
    }

    public Long getTargetAnalysisId() {
        return targetAnalysisId;
    }

    public void setTargetAnalysisId(Long targetAnalysisId) {
        this.targetAnalysisId = targetAnalysisId;
    }
}
