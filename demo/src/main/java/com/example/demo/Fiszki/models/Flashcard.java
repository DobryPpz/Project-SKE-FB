package com.example.demo.Fiszki.models;

public class Flashcard {
    private int id;
    private int flashcardSetId;
    private String front;
    private String back;

    public Flashcard(int flashcardSetId ,String front, String back) {
        this.flashcardSetId = flashcardSetId;
        this.front = front;
        this.back = back;
    }

    public int getFlashcardSetId() {
        return flashcardSetId;
    }

    public void setFlashcardSetId(int flashcardSetId) {
        this.flashcardSetId = flashcardSetId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }
}
