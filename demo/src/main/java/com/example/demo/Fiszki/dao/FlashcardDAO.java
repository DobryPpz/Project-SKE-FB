package com.example.demo.Fiszki.dao;
import com.example.demo.Fiszki.models.Flashcard;
import java.util.List;

public interface FlashcardDAO {
    List<Flashcard> findAll();
    Flashcard findById(int theId);
    Flashcard save(Flashcard flashcard);
    void deleteById(int theId);
}
