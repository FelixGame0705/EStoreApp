package com.group5.estoreapp.model;

import java.util.List;

public class ChatHub {
    private int id;
    private int userId;
    private int storeId; // nếu không dùng thì có thể bỏ
    private List<ChatMessage> messages;

    public ChatHub() {
    }

    public ChatHub(int id, int userId, int storeId, List<ChatMessage> messages) {
        this.id = id;
        this.userId = userId;
        this.storeId = storeId;
        this.messages = messages;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getStoreId() {
        return storeId;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }
}
