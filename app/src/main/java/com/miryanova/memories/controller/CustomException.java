package com.miryanova.memories.controller;

public class CustomException extends Exception {
    String msg;

    CustomException(String _msg) {
        msg = _msg;
    }

    public String getMessage() {
        return msg;
    }
}