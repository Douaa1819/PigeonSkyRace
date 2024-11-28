package com.pigeonskyrace.utils;

import org.springframework.http.HttpStatus;

public class ResponseApi<T> {

    private T data;
    private String message;
    private int status;

    public ResponseApi(T data, String message, HttpStatus status) {
        this.data = data;
        this.message = message;
        this.status = status.value();
    }

    // Getters and Setters
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
