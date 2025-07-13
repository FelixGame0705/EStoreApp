package com.group5.estoreapp.model;

public class PaymentRequest {
    private int orderID;
    private String paymentMethod;
    private String returnUrl;
    private String cancelUrl;
    private String language;

    public PaymentRequest(int orderID, String paymentMethod, String returnUrl, String cancelUrl, String language) {
        this.orderID = orderID;
        this.paymentMethod = paymentMethod;
        this.returnUrl = returnUrl;
        this.cancelUrl = cancelUrl;
        this.language = language;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getCancelUrl() {
        return cancelUrl;
    }

    public void setCancelUrl(String cancelUrl) {
        this.cancelUrl = cancelUrl;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
