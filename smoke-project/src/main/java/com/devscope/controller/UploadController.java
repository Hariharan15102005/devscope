package com.devscope.controller;

import com.devscope.dto.response.UploadResponse;
import com.devscope.exception.AnalysisException;
import com.devscope.service.AnalysisService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*")
public class UploadController {

    private static final long MAX_UPLOAD_BYTES = 25L * 1024L * 1024L;

    private final AnalysisService analysisService;

    public UploadController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @PostMapping("/upload")
    public UploadResponse uploadProject(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new AnalysisException("Uploaded file must not be empty");
        }

        String filename = file.getOriginalFilename();
        if (filename == null || !filename.toLowerCase().endsWith(".zip")) {
            throw new AnalysisException("Only .zip project archives are supported");
        }

        if (file.getSize() > MAX_UPLOAD_BYTES) {
            throw new AnalysisException("Uploaded ZIP exceeds max size limit of 25MB");
        }

        Long analysisId = analysisService.processUpload(file);
        return new UploadResponse(analysisId);
    }
}
