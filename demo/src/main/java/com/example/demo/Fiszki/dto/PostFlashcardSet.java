package com.example.demo.Fiszki.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class PostFlashcardSet {
    public String getName() {
        return name;
    }

    public PostFlashcardSet(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Schema(description = "name",example = "myset1")
    private String name;

}
