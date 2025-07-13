package com.group5.estoreapp.model;

public class ApiResponse<T> {
    private boolean isSuccess;
    private T result;

    public boolean isSuccess() {
        return isSuccess;
    }

    public T getResult() {
        return result;
    }
}