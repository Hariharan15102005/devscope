package com.devscope.service;

import com.devscope.dto.response.InsightResponse;
import com.devscope.mapper.InsightMapper;
import com.devscope.repository.InsightRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InsightsService {

    private final InsightRepository insightRepository;
    private final InsightMapper insightMapper;

    public InsightsService(InsightRepository insightRepository, InsightMapper insightMapper) {
        this.insightRepository = insightRepository;
        this.insightMapper = insightMapper;
    }

    public List<InsightResponse> getInsights(Long analysisId) {
        return insightRepository.findByAnalysisRun_Id(analysisId).stream()
                .map(insightMapper::toResponse)
                .toList();
    }
}
