package com.devscope.repository;

import com.devscope.model.AnalysisRun;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnalysisRunRepository extends JpaRepository<AnalysisRun, Long> {
    Optional<AnalysisRun> findByRunKey(String runKey);

    List<AnalysisRun> findByProject_ProjectKeyOrderByCreatedAtDesc(String projectKey);
}
