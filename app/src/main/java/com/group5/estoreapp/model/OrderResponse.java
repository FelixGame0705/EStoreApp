package com.group5.estoreapp.model;

import com.google.gson.annotations.SerializedName;

public class OrderResponse {
    @SerializedName("orderID")
    private int orderId;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}