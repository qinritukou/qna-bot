package com.infosupport.demos.qnabot.service;

public class ApplicationError {
    private final String message;

    public ApplicationError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
