package com.devscope.repository;

import com.devscope.model.JavaClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JavaClassRepository extends JpaRepository<JavaClassEntity, Long> {
    List<JavaClassEntity> findByAnalysisRun_RunKey(String runKey);

    List<JavaClassEntity> findByAnalysisRun_Id(Long analysisId);

    Optional<JavaClassEntity> findByAnalysisRun_RunKeyAndFullName(String runKey, String fullName);

    Optional<JavaClassEntity> findByAnalysisRun_IdAndFullName(Long analysisId, String fullName);
}
