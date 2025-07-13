package com.group5.estoreapp.model;

public class ChatMessage {
    private int id;
    private int chatHubId;
    private int senderId;
    private String senderName;
    private String message;
    private String sentAt;

    public ChatMessage() {
    }

    public ChatMessage(int chatHubId, int senderId, String message) {
        this.chatHubId = chatHubId;
        this.senderId = senderId;
        this.message = message;
    }

    // Getter
    public int getId() {
        return id;
    }

    public int getChatHubId() {
        return chatHubId;
    }

    public int getSenderId() {
        return senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getMessage() {
        return message;
    }

    public String getSentAt() {
        return sentAt;
    }

    // Setter
    public void setId(int id) {
        this.id = id;
    }

    public void setChatHubId(int chatHubId) {
        this.chatHubId = chatHubId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSentAt(String sentAt) {
        this.sentAt = sentAt;
    }
}
