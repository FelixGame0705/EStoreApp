package com.group5.estoreapp.model;

public class ChatMessageRequest {
    private int chatHubId;
    private int senderId;
    private String message;

    public ChatMessageRequest(int chatHubId, int senderId, String message) {
        this.chatHubId = chatHubId;
        this.senderId = senderId;
        this.message = message;
    }
}
