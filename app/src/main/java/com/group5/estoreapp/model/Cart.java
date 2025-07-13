package com.group5.estoreapp.model;

import java.util.List;

public class Cart {
    private int cartID;
    private int userID;
    private double totalPrice;
    private String status;
    private String userName;
    private List<CartItem> cartItems;

    // ✅ Getter cho cartItems
    public List<CartItem> getCartItems() {
        return cartItems;
    }

    // ✅ Thêm các getter & setter khác nếu cần
    public int getCartID() {
        return cartID;
    }

    public void setCartID(int cartID) {
        this.cartID = cartID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }
}
