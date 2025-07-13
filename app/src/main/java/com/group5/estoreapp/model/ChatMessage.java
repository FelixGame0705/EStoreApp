package com.group5.estoreapp.model;

public class ChatMessage {
    private int userID;
    private String message;
    private String sentAt;

    public ChatMessage(int userID, String message, String sentAt) {
        this.userID = userID;
        this.message = message;
        this.sentAt = sentAt;
    }

    public int getUserID() {
        return userID;
    }

    public String getMessage() {
        return message;
    }

    public String getSentAt() {
        return sentAt;
    }
}
