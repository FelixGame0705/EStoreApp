package com.group5.estoreapp.model;

public class PaymentResponse {
    private boolean success;
    private String message;
    private String paymentUrl;
    private String transactionId;

    // ✅ Các trường bổ sung từ API GET /Payments/order/{orderId}
    private int paymentID;
    private int orderID;
    private long amount;
    private String paymentDate;
    private String paymentStatus; // 👈 Dùng để check "Completed"

    // GETTERS
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public int getPaymentID() {
        return paymentID;
    }

    public int getOrderID() {
        return orderID;
    }

    public long getAmount() {
        return amount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }
}
