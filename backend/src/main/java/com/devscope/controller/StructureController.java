package com.devscope.controller;

import com.devscope.dto.response.ClassDetailResponse;
import com.devscope.dto.response.StructureTreeResponse;
import com.devscope.service.StructureService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/structure")
@CrossOrigin(origins = "*")
public class StructureController {

    private final StructureService structureService;

    public StructureController(StructureService structureService) {
        this.structureService = structureService;
    }

    @GetMapping("/{projectId}")
    public StructureTreeResponse getStructure(
            @PathVariable String projectId,
            @RequestParam(required = false) String filter
    ) {
        return structureService.getStructureTree(projectId, filter);
    }

    @GetMapping("/{projectId}/class")
    public ResponseEntity<ClassDetailResponse> getClassDetail(
            @PathVariable String projectId,
            @RequestParam("name") String className
    ) {
        ClassDetailResponse detail = structureService.getClassDetail(projectId, className);
        if (detail == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(detail);
    }
}
