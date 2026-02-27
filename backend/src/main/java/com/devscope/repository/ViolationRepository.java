package com.devscope.repository;

import com.devscope.model.ViolationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ViolationRepository extends JpaRepository<ViolationEntity, Long> {
    List<ViolationEntity> findByAnalysisRun_RunKey(String runKey);
}
