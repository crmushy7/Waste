package com.example.wmeaapp;

public interface ResponseCallback {
    void onResponse(String response);
    void onError(Throwable throwable);
}
