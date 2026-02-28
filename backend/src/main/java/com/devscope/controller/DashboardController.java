package com.devscope.controller;

import com.devscope.dto.response.DashboardResponse;
import com.devscope.service.DashboardService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/{analysisId}")
    public DashboardResponse getSummary(@PathVariable Long analysisId) {
        return dashboardService.getDashboardSummary(analysisId);
    }

    @GetMapping(params = "analysisId")
    public DashboardResponse getSummaryByQuery(@RequestParam Long analysisId) {
        return dashboardService.getDashboardSummary(analysisId);
    }
}
