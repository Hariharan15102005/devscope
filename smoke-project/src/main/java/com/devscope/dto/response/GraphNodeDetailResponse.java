package com.devscope.dto.response;

import java.util.List;

public class GraphNodeDetailResponse {
    private String id;
    private String name;
    private String fullName;
    private String type;
    private int dependencyCount;
    private int dependentCount;
    private List<String> outgoingDependencies;
    private List<String> incomingDependents;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDependencyCount() {
        return dependencyCount;
    }

    public void setDependencyCount(int dependencyCount) {
        this.dependencyCount = dependencyCount;
    }

    public int getDependentCount() {
        return dependentCount;
    }

    public void setDependentCount(int dependentCount) {
        this.dependentCount = dependentCount;
    }

    public List<String> getOutgoingDependencies() {
        return outgoingDependencies;
    }

    public void setOutgoingDependencies(List<String> outgoingDependencies) {
        this.outgoingDependencies = outgoingDependencies;
    }

    public List<String> getIncomingDependents() {
        return incomingDependents;
    }

    public void setIncomingDependents(List<String> incomingDependents) {
        this.incomingDependents = incomingDependents;
    }
}