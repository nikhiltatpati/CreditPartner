package com.app.creditpartner.Classes;

public class Notifications {

    String notificationTitle, notificationText, notificationDate;


    public Notifications(String notificationTitle, String notificationText, String notificationDate) {
        this.notificationTitle = notificationTitle;
        this.notificationText = notificationText;
        this.notificationDate = notificationDate;
    }

    public Notifications() {
    }

    public String getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(String notificationDate) {
        this.notificationDate = notificationDate;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }
}
