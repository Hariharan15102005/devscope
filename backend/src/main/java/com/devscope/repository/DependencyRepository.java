package com.devscope.repository;

import com.devscope.model.DependencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DependencyRepository extends JpaRepository<DependencyEntity, Long> {
    List<DependencyEntity> findByAnalysisRun_RunKey(String runKey);
}
