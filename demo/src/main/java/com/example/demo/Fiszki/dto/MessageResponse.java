package com.example.demo.Fiszki.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public class MessageResponse {
    public String getMessage() {
        return message;
    }

    public MessageResponse(String message) {
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Schema(description = "message",example = "some string message to user")
    private String message;

}
