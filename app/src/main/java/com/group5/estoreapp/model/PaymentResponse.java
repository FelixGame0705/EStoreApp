package com.group5.estoreapp.model;

public class PaymentResponse {
    private boolean success;
    private String message;
    private String paymentUrl;
    private String transactionId;

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
}
