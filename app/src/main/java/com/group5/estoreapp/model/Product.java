package com.group5.estoreapp.model;

import com.google.gson.annotations.SerializedName;

public class Product {
    // Trường cho dữ liệu API
    @SerializedName("productID")
    private int productID;

    @SerializedName("productName")
    private String productName;

    @SerializedName("briefDescription")
    private String briefDescription;

    @SerializedName("fullDescription")
    private String fullDescription;

    @SerializedName("technicalSpecifications")
    private String technicalSpecifications;


    @SerializedName("price")
    private double price;

    @SerializedName("imageURL")
    private String imageURL; // URL từ API

    @SerializedName("categoryID")
    private int categoryID;

    @SerializedName("categoryName")
    private String categoryName;

    // Constructor cho dữ liệu local (mock)
    public int getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }

    public String getBriefDescription() {
        return briefDescription;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public String getTechnicalSpecifications() {
        return technicalSpecifications;
    }

    public double getPrice() {
        return price;
    }

    public String getImageURL() {
        return imageURL;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }
    @Override
    public String toString() {
        return "Product{" +
                "productID=" + productID +
                ", productName='" + productName + '\'' +
                ", briefDescription='" + briefDescription + '\'' +
                ", fullDescription='" + fullDescription + '\'' +
                ", technicalSpecifications='" + technicalSpecifications + '\'' +
                ", price=" + price +
                ", imageURL='" + imageURL + '\'' +
                ", categoryID=" + categoryID +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}
