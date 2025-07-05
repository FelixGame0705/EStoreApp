package com.group5.estoreapp.model;

public class OrderRequest {
    private int cartID;
    private int userID;
    private String paymentMethod;
    private String billingAddress;

    public OrderRequest(int cartID, int userID, String paymentMethod, String billingAddress) {
        this.cartID = cartID;
        this.userID = userID;
        this.paymentMethod = paymentMethod;
        this.billingAddress = billingAddress;
    }

    // Getters and setters
    public int getCartID() { return cartID; }
    public void setCartID(int cartID) { this.cartID = cartID; }

    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getBillingAddress() { return billingAddress; }
    public void setBillingAddress(String billingAddress) { this.billingAddress = billingAddress; }
}
