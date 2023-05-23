package com.example.demo.Fiszki.service;

import com.example.demo.Fiszki.models.Flashcard;
import com.example.demo.Fiszki.models.FlashcardSet;

import java.util.List;

public interface FlashcardSetService {
    List<FlashcardSet> findAll();
    FlashcardSet findById(int theId);
    FlashcardSet save(FlashcardSet flashcardSet);
    List<FlashcardSet> findAllByUser(String username);
    void deleteById(int theId);
}
