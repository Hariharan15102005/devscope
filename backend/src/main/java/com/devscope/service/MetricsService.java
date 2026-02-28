package com.devscope.service;

import com.devscope.dto.response.MetricResponse;
import com.devscope.repository.MetricRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetricsService {

    private final MetricRepository metricRepository;

    public MetricsService(MetricRepository metricRepository) {
        this.metricRepository = metricRepository;
    }

    public List<MetricResponse> getMetrics(Long analysisId) {
        return metricRepository.findByAnalysisRun_Id(analysisId).stream().map(metric -> {
            MetricResponse response = new MetricResponse();
            response.setClassName(metric.getClassName());
            response.setLinesOfCode(metric.getLinesOfCode());
            response.setMethodCount(metric.getMethodCount());
            response.setDependencyCount(metric.getDependencyCount());
            response.setRiskLevel(metric.getRiskLevel());
            return response;
        }).toList();
    }
}
