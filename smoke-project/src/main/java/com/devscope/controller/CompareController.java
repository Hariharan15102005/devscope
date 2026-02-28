package com.devscope.controller;

import com.devscope.dto.request.CompareRequest;
import com.devscope.dto.response.CompareResponse;
import com.devscope.service.CompareService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/compare")
@CrossOrigin(origins = "*")
public class CompareController {

    private final CompareService compareService;

    public CompareController(CompareService compareService) {
        this.compareService = compareService;
    }

    @PostMapping
    public CompareResponse compare(@RequestBody CompareRequest request) {
        return compareService.compare(request.getBaseAnalysisId(), request.getTargetAnalysisId());
    }

    @GetMapping("/versions/{analysisId}")
    public List<String> getAvailableVersions(@PathVariable Long analysisId) {
        return compareService.getAvailableAnalyses(analysisId);
    }
}
