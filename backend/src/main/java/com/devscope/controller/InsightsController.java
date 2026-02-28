package com.devscope.controller;

import com.devscope.dto.response.InsightResponse;
import com.devscope.service.InsightsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/insights")
@CrossOrigin(origins = "*")
public class InsightsController {

    private final InsightsService insightsService;

    public InsightsController(InsightsService insightsService) {
        this.insightsService = insightsService;
    }

    @GetMapping("/{analysisId}")
    public List<InsightResponse> getInsights(@PathVariable Long analysisId) {
        return insightsService.getInsights(analysisId);
    }

    @GetMapping(params = "analysisId")
    public List<InsightResponse> getInsightsByQuery(@RequestParam Long analysisId) {
        return insightsService.getInsights(analysisId);
    }
}
