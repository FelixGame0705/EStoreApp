package com.group5.estoreapp.model;

import java.io.Serializable;

public class CartItem  implements Serializable {
    private int cartItemID;
    private int userID;
    private int cartID;
    private int productID;
    private int quantity;
    private int price;
    private String productName;
    private String productImageURL;

    // Getters
    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }
    public int getCartItemID() {
        return cartItemID;
    }

    public int getCartID() {
        return cartID;
    }

    public int getProductID() {
        return productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductImageURL() {
        return productImageURL;
    }

    // Setters
    public void setCartItemID(int cartItemID) {
        this.cartItemID = cartItemID;
    }

    public void setCartID(int cartID) {
        this.cartID = cartID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductImageURL(String productImageURL) {
        this.productImageURL = productImageURL;
    }
}
