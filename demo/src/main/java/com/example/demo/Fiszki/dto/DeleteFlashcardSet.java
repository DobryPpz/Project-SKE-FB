package com.example.demo.Fiszki.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class DeleteFlashcardSet {
    public int getSet_id() {
        return set_id;
    }

    public DeleteFlashcardSet(int set_id) {
        this.set_id = set_id;
    }

    public void setSet_id(int set_id) {
        this.set_id = set_id;
    }

    @Schema(description = "set_id",example = "1")
    private int set_id;

    public DeleteFlashcardSet() {
    }
}
