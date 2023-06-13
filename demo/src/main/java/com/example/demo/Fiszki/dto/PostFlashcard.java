package com.example.demo.Fiszki.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class PostFlashcard {

    @Schema(description = "set_id",example = "1")
    private int set_id;
    @Schema(description = "front",example = "kot")
    private String front;

    public int getSet_id() {
        return set_id;
    }

    public void setSet_id(int set_id) {
        this.set_id = set_id;
    }

    public PostFlashcard() {
    }

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public PostFlashcard(int set_id, String front, String back) {
        this.set_id = set_id;
        this.front = front;
        this.back = back;
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }

    @Schema(description = "back",example = "cat")
    private String back;

}
