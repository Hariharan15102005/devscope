package com.devscope.controller;

import com.devscope.service.AnalysisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*")
public class UploadController {

    private static final Logger log = LoggerFactory.getLogger(UploadController.class);

    private final AnalysisService analysisService;

    public UploadController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadProject(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        if (file.getOriginalFilename() == null || !file.getOriginalFilename().toLowerCase().endsWith(".zip")) {
            throw new IllegalArgumentException("Only ZIP files allowed");
        }

        log.info("File received: {}", file.getOriginalFilename());

        Long analysisId = analysisService.processUpload(file);

        return ResponseEntity.ok(Map.of("analysisId", analysisId));
    }
}
