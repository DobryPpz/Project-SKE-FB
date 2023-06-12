package com.example.demo.Fiszki.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class DeleteFlashcard {

    @Schema(description = "id",example = "1")
    private int id;

    public DeleteFlashcard(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
