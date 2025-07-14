// NotificationRequest.java
package com.group5.estoreapp.model;

public class NotificationRequest {
    private String message;
    private int userID;

    public NotificationRequest(String message, int userID) {
        this.message = message;
        this.userID = userID;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }
}
