package com.example.demo.Fiszki.service;
import com.example.demo.Fiszki.dao.FlashcardSetDAO;
import com.example.demo.Fiszki.models.FlashcardSet;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FlashcardSetServiceImpl implements FlashcardSetService {

    private FlashcardSetDAO flashcardSetDAO;

    @Autowired
    public FlashcardSetServiceImpl(FlashcardSetDAO flashcardSetDAO){
        this.flashcardSetDAO = flashcardSetDAO;
    }

    @Override
    public List<FlashcardSet> findAll() {
        return flashcardSetDAO.findAll();
    }

    @Override
    public FlashcardSet findById(int theId) {
        return flashcardSetDAO.findById(theId);
    }

    @Override
    @Transactional
    public FlashcardSet save(FlashcardSet flashcardSet) {
        return flashcardSetDAO.save(flashcardSet);
    }

    @Override
    public List<FlashcardSet> findAllByUser(String username) {
        return flashcardSetDAO.findAllByUser(username);
    }

    @Override
    @Transactional
    public void deleteById(int theId) {
        flashcardSetDAO.deleteById(theId);
    }
}
