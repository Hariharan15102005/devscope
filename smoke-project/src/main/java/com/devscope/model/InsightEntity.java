package com.devscope.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "insights")
public class InsightEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "analysis_run_id", nullable = false)
    private AnalysisRun analysisRun;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private String impactLevel;

    @Column(nullable = false)
    private String category;

    @Column(length = 2000)
    private String reasoning;

    @Column(length = 2000)
    private String affectedClassesCsv;

    @Column(length = 2000)
    private String recommendationStepsCsv;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AnalysisRun getAnalysisRun() {
        return analysisRun;
    }

    public void setAnalysisRun(AnalysisRun analysisRun) {
        this.analysisRun = analysisRun;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImpactLevel() {
        return impactLevel;
    }

    public void setImpactLevel(String impactLevel) {
        this.impactLevel = impactLevel;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getReasoning() {
        return reasoning;
    }

    public void setReasoning(String reasoning) {
        this.reasoning = reasoning;
    }

    public String getAffectedClassesCsv() {
        return affectedClassesCsv;
    }

    public void setAffectedClassesCsv(String affectedClassesCsv) {
        this.affectedClassesCsv = affectedClassesCsv;
    }

    public String getRecommendationStepsCsv() {
        return recommendationStepsCsv;
    }

    public void setRecommendationStepsCsv(String recommendationStepsCsv) {
        this.recommendationStepsCsv = recommendationStepsCsv;
    }
}
