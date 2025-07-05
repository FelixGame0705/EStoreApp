package com.group5.estoreapp.model;

public class Category {
    private String name;
    private int iconRes;

    public Category(String name, int iconRes) {
        this.name = name;
        this.iconRes = iconRes;
    }

    public String getName() {
        return name;
    }

    public int getIconRes() {
        return iconRes;
    }
}
