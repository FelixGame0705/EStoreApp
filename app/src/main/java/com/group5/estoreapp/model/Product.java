package com.group5.estoreapp.model;

import com.google.gson.annotations.SerializedName;

public class Product {
    // Trường cho dữ liệu API
    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("price")
    private double price;

    @SerializedName("image")
    private String image; // URL từ API

    @SerializedName("description")
    private String description;

    @SerializedName("category")
    private String category;

    // Trường cho dữ liệu local
    private int imageResourceId;

    // Constructor cho dữ liệu local (mock)
    public Product(String title, double price, int imageResourceId) {
        this.title = title;
        this.price = price;
        this.imageResourceId = imageResourceId;
    }

    // Constructor cho dữ liệu từ API
    public Product(int id, String title, double price, String image, String description, String category) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.image = image;
        this.description = description;
        this.category = category;
    }

    // Getters cho API
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    // Getter cho ảnh local
    public int getImageResourceId() {
        return imageResourceId;
    }

    // Setters (nếu cần)
    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", imageResourceId=" + imageResourceId +
                '}';
    }
}
