package com.group5.estoreapp.model;

public class ChatHubRequest {
    private int secondUserId;

    public ChatHubRequest(int secondUserId) {
        this.secondUserId = secondUserId;
    }

    public int getSecondUserId() {
        return secondUserId;
    }

    public void setSecondUserId(int secondUserId) {
        this.secondUserId = secondUserId;
    }
}



