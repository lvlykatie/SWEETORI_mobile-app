package com.example.sweetori;

public class APIResponse<T> {
    private int statusCode;
    private String error;
    private Object message;
    private T data;

    public int getStatusCode() {
        return statusCode;
    }
    public String getError() {
        return error;
    }
    public Object getMessage() {
        return message;
    }
    public T getData() {
        return data;
    }
}
