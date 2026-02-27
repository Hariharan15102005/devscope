package com.devscope.controller;

import com.devscope.dto.response.GraphNodeDetailResponse;
import com.devscope.dto.response.GraphResponse;
import com.devscope.service.GraphService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/graph")
@CrossOrigin(origins = "*")
public class GraphController {

    private final GraphService graphService;

    public GraphController(GraphService graphService) {
        this.graphService = graphService;
    }

    @GetMapping("/{projectId}")
    public GraphResponse getGraph(@PathVariable String projectId) {
        return graphService.getGraph(projectId);
    }

    @GetMapping("/{projectId}/node/{nodeId}")
    public ResponseEntity<GraphNodeDetailResponse> getNodeDetail(
            @PathVariable String projectId,
            @PathVariable String nodeId
    ) {
        GraphNodeDetailResponse detail = graphService.getNodeDetails(projectId, nodeId);
        if (detail == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(detail);
    }
}