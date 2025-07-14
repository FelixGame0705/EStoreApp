package com.group5.estoreapp.model;

public class ChatMessageRequest {
    private String chatHubId;
    private String content;
    private int type;

    public ChatMessageRequest(String chatHubId, String content, int type) {
        this.chatHubId = chatHubId;
        this.content = content;
        this.type = type;
    }

    // Getters and Setters
    public String getChatHubId() {
        return chatHubId;
    }

    public void setChatHubId(String chatHubId) {
        this.chatHubId = chatHubId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
