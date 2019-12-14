package com.example.creditpartner.Classes;

public class Applications {

    private String appImage, appName, appDate;

    public Applications() {
    }


    public Applications(String appImage, String appName, String appDate) {
        this.appImage = appImage;
        this.appName = appName;
        this.appDate = appDate;
    }

    public String getAppImage() {
        return appImage;
    }

    public void setAppImage(String appImage) {
        this.appImage = appImage;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppDate() {
        return appDate;
    }

    public void setAppDate(String appDate) {
        this.appDate = appDate;
    }
}
