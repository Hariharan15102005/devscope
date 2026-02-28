package com.devscope.repository;

import com.devscope.model.MetricEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MetricRepository extends JpaRepository<MetricEntity, Long> {
    List<MetricEntity> findByAnalysisRun_RunKey(String runKey);

    List<MetricEntity> findByAnalysisRun_Id(Long analysisId);
}
