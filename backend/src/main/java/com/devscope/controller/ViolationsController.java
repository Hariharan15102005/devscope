package com.devscope.controller;

import com.devscope.dto.response.ViolationResponse;
import com.devscope.service.ViolationsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/violations")
@CrossOrigin(origins = "*")
public class ViolationsController {

    private final ViolationsService violationsService;

    public ViolationsController(ViolationsService violationsService) {
        this.violationsService = violationsService;
    }

    @GetMapping("/{analysisId}")
    public List<ViolationResponse> getViolations(@PathVariable Long analysisId) {
        return violationsService.getViolations(analysisId);
    }

    @GetMapping(params = "analysisId")
    public List<ViolationResponse> getViolationsByQuery(@RequestParam Long analysisId) {
        return violationsService.getViolations(analysisId);
    }
}
