package com.example.demo.Fiszki.service;
import com.example.demo.Fiszki.models.Flashcard;
import java.util.List;

public interface FlashcardService {
    List<Flashcard> findAll();
    Flashcard findById(int theId);
    Flashcard save(Flashcard flashcard);
    void deleteById(int theId);
}
