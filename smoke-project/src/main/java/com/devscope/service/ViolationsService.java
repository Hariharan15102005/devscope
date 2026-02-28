package com.devscope.service;

import com.devscope.dto.response.ViolationResponse;
import com.devscope.mapper.ViolationMapper;
import com.devscope.repository.ViolationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViolationsService {

    private final ViolationRepository violationRepository;
    private final ViolationMapper violationMapper;

    public ViolationsService(ViolationRepository violationRepository, ViolationMapper violationMapper) {
        this.violationRepository = violationRepository;
        this.violationMapper = violationMapper;
    }

    public List<ViolationResponse> getViolations(Long analysisId) {
        return violationRepository.findByAnalysisRun_Id(analysisId).stream()
                .map(violationMapper::toResponse)
                .toList();
    }
}
