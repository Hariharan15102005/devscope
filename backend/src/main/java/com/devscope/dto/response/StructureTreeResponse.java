package com.devscope.dto.response;

import java.util.List;

public class StructureTreeResponse {
    private List<PackageNode> packages;

    public List<PackageNode> getPackages() {
        return packages;
    }

    public void setPackages(List<PackageNode> packages) {
        this.packages = packages;
    }

    public static class PackageNode {
        private String packageName;
        private List<ClassNode> classes;

        public PackageNode() {
        }

        public PackageNode(String packageName, List<ClassNode> classes) {
            this.packageName = packageName;
            this.classes = classes;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public List<ClassNode> getClasses() {
            return classes;
        }

        public void setClasses(List<ClassNode> classes) {
            this.classes = classes;
        }
    }

    public static class ClassNode {
        private String fullName;
        private String className;
        private String annotation;

        public ClassNode() {
        }

        public ClassNode(String fullName, String className, String annotation) {
            this.fullName = fullName;
            this.className = className;
            this.annotation = annotation;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getAnnotation() {
            return annotation;
        }

        public void setAnnotation(String annotation) {
            this.annotation = annotation;
        }
    }
}
