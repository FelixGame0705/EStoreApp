package com.group5.estoreapp.model;

import com.google.gson.annotations.SerializedName;

public class ChatMessage {
    private String id;
    private int chatHubId;
    private int senderId;
    private String senderName;
    private String content;
    @SerializedName("createdTime")
    private String sentAt;

    public ChatMessage() {
    }

    public ChatMessage(int chatHubId, int senderId, String content) {
        this.chatHubId = chatHubId;
        this.senderId = senderId;
        this.content = content;
    }

    // Getter
    public String getId() {
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

    public String getContent() {
        return content;
    }

    public String getSentAt() {
        return sentAt;
    }

    // Setter
    public void setId(String id) {
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

    public void setContent(String content) {
        this.content = content;
    }

    public void setSentAt(String sentAt) {
        this.sentAt = sentAt;
    }
}
