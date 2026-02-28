package com.devscope.exception;

public class ProjectNotFoundException extends RuntimeException {

    public ProjectNotFoundException(String projectId) {
        super("Project not found: " + projectId);
    }
}
