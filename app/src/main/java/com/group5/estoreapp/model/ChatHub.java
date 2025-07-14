package com.group5.estoreapp.model;

import java.util.List;

public class ChatHub {
    private String id; // 👈 UUID dạng String
    private String status;
    private String createdTime;
    private String updatedTime;
    private int fUserId;
    private int sUserId;
    private List<ChatMessage> chatMessages;

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public int getFUserId() {
        return fUserId;
    }

    public int getSUserId() {
        return sUserId;
    }

    public List<ChatMessage> getMessages() {
        return chatMessages;
    }

    // Setters...
}
