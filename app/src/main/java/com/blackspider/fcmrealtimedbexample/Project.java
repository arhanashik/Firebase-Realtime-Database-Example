package com.blackspider.fcmrealtimedbexample;

public class Project {
    private String projectId;
    private String projectName;
    private String platform;

    public Project() {
    }

    public Project(String projectId, String projectName, String platform) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.platform = platform;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
