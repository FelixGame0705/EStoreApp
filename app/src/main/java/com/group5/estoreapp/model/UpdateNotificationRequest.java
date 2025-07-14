package com.group5.estoreapp.model;

public class UpdateNotificationRequest {
    private String message;
    private boolean isRead;

    public UpdateNotificationRequest(String message, boolean isRead) {
        this.message = message;
        this.isRead = isRead;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
}