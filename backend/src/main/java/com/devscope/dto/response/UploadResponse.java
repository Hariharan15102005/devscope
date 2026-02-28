package com.devscope.dto.response;

public class UploadResponse {
    private Long analysisId;

    public UploadResponse() {
    }

    public UploadResponse(Long analysisId) {
        this.analysisId = analysisId;
    }

    public Long getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(Long analysisId) {
        this.analysisId = analysisId;
    }
}
