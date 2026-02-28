package com.devscope.repository;

import com.devscope.model.InsightEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InsightRepository extends JpaRepository<InsightEntity, Long> {
    List<InsightEntity> findByAnalysisRun_RunKey(String runKey);

    List<InsightEntity> findByAnalysisRun_Id(Long analysisId);
}
