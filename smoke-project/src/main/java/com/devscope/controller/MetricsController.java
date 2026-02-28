package com.devscope.controller;

import com.devscope.dto.response.MetricResponse;
import com.devscope.service.MetricsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/metrics")
@CrossOrigin(origins = "*")
public class MetricsController {

    private final MetricsService metricsService;

    public MetricsController(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @GetMapping("/{analysisId}")
    public List<MetricResponse> getMetrics(@PathVariable Long analysisId) {
        return metricsService.getMetrics(analysisId);
    }
}
