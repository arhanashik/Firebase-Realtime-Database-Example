package com.blackspider.fcmrealtimedbexample;

import java.io.Serializable;

public class Developer implements Serializable{

    private String developerId;
    private String developerName;
    private String developerExpert;

    public Developer() {
    }

    public Developer(String developerId, String developerName, String developerExpert) {
        this.developerId = developerId;
        this.developerName = developerName;
        this.developerExpert = developerExpert;
    }

    public String getDeveloperId() {
        return developerId;
    }

    public void setDeveloperId(String developerId) {
        this.developerId = developerId;
    }

    public String getDeveloperName() {
        return developerName;
    }

    public void setDeveloperName(String developerName) {
        this.developerName = developerName;
    }

    public String getDeveloperExpert() {
        return developerExpert;
    }

    public void setDeveloperExpert(String developerExpert) {
        this.developerExpert = developerExpert;
    }
}
