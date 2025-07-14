package com.group5.estoreapp.model;

import com.google.gson.annotations.SerializedName;

public class Notification {
    private String id;

    @SerializedName("notificationID")
    private int notificationID;

    @SerializedName("message")
    private String message;

    @SerializedName("userID")
    private int userID;

    @SerializedName("isRead")
    private boolean isRead;

    @SerializedName("createdTime")
    private String createdTime;

    @SerializedName("updatedTime")
    private String updatedTime;

    public Notification() {}

    public Notification(int notificationID, String message, int userID) {
        this.notificationID = notificationID;
        this.message = message;
        this.userID = userID;
        this.isRead = false;
    }

    // Getters
    public String getId() { return id; }
    public int getNotificationID() { return notificationID; }
    public String getMessage() { return message; }
    public int getUserID() { return userID; }
    public boolean isRead() { return isRead; }
    public String getCreatedTime() { return createdTime; }
    public String getUpdatedTime() { return updatedTime; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setNotificationID(int notificationID) { this.notificationID = notificationID; }
    public void setMessage(String message) { this.message = message; }
    public void setUserID(int userID) { this.userID = userID; }
    public void setRead(boolean read) { isRead = read; }
    public void setCreatedTime(String createdTime) { this.createdTime = createdTime; }
    public void setUpdatedTime(String updatedTime) { this.updatedTime = updatedTime; }
}


// UpdateNotificationRequest.java
