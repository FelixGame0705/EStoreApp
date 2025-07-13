package com.group5.estoreapp.model;

import java.util.Date;

public class Order {
    private int orderID;
    private int cartID;
    private int userID;
    private String paymentMethod;
    private String billingAddress;
    private String orderStatus;
    private Date orderDate;
    private double totalAmount;

//    private User user;
    private Cart cart;

    // Getter và Setter
    public int getOrderID() { return orderID; }
    public void setOrderID(int orderID) { this.orderID = orderID; }

    public int getCartID() { return cartID; }
    public void setCartID(int cartID) { this.cartID = cartID; }

    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getBillingAddress() { return billingAddress; }
    public void setBillingAddress(String billingAddress) { this.billingAddress = billingAddress; }

    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }

    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

//    public User getUser() { return user; }
//    public void setUser(User user) { this.user = user; }

    public Cart getCart() { return cart; }
    public void setCart(Cart cart) { this.cart = cart; }
}

