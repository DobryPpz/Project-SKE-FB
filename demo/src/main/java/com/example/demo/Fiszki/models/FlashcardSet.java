package com.example.demo.Fiszki.models;
import java.util.ArrayList;
import java.util.List;

public class FlashcardSet {

    private int id;
    private String name;
    private List<Flashcard> flashcards;

    public FlashcardSet(String name) {
        this.name = name;
        this.flashcards = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Flashcard> getFlashcards() {
        return flashcards;
    }

    public void setFlashcards(List<Flashcard> flashcards) {
        this.flashcards = flashcards;
    }

    public Flashcard addFlashcard(Flashcard flashcard){
        this.flashcards.add(flashcard);
        return flashcard;
    }

    public void removeFlashcard(Flashcard flashcard){
        this.flashcards.remove(flashcard);
    }

    public void removeFlashcard(int id){
        this.flashcards.remove(id);
    }
}
