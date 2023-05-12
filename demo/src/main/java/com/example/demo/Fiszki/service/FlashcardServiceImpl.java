package com.example.demo.Fiszki.service;
import com.example.demo.Fiszki.dao.FlashcardDAO;
import com.example.demo.Fiszki.dao.FlashcardDAOImpl;
import com.example.demo.Fiszki.models.Flashcard;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlashcardServiceImpl implements FlashcardService {

    private FlashcardDAO flashcardDAO;

    @Autowired
    public FlashcardServiceImpl(FlashcardDAO flashcardDAO){
        this.flashcardDAO = flashcardDAO;
    }
    @Override
    public List<Flashcard> findAll() {
        return flashcardDAO.findAll();
    }

    @Override
    public Flashcard findById(int theId) {
        return flashcardDAO.findById(theId);
    }

    @Override
    @Transactional
    public Flashcard save(Flashcard flashcard) {
        return flashcardDAO.save(flashcard);
    }

    @Override
    @Transactional
    public void deleteById(int theId) {
        flashcardDAO.deleteById(theId);
    }
}
