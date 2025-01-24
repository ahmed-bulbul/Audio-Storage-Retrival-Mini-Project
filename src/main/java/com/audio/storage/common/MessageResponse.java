package com.audio.storage.common;

import org.springframework.core.io.Resource;

public class MessageResponse {
    private String message;

    Long id;

    public MessageResponse(String message) {
        this.message = message;
    }

    public MessageResponse(String message, Long id) {
        this.message = message;
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
