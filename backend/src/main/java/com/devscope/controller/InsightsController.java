package com.devscope.controller;

import com.devscope.dto.response.InsightResponse;
import com.devscope.service.InsightsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/{projectId}")
    public List<InsightResponse> getInsights(@PathVariable String projectId) {
        return insightsService.getInsights(projectId);
    }
}
