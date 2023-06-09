package com.example.demo.Wordle.service;

import com.example.demo.Fiszki.models.FlashcardSet;
import com.example.demo.Wordle.dao.WordleDAO;
import com.example.demo.Wordle.models.WordleGame;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class WordleServiceImpl implements WordleService{

    private WordleDAO wordleDAO;

    @Autowired
    public WordleServiceImpl(WordleDAO wordleDAO) {
        this.wordleDAO = wordleDAO;
    }

    @Override
    @Transactional
    public ResponseEntity<?> newGame(FlashcardSet flashcardSet,String side, String email) {
        return wordleDAO.newGame(flashcardSet,side,email);
    }
    @Override
    @Transactional
    public List<WordleGame> getAllCurrentGames(String email) {
        return wordleDAO.getAllCurrentGames(email);
    }
    @Override
    @Transactional
    public ResponseEntity<?> getGivenGame(String gameID,String email)  {
        return wordleDAO.getGivenGame(gameID,email);
    }

    @Override
    @Transactional
    public ResponseEntity<?> makeGuess (Map<String,String> jsonWithIDandGuess,String email) throws Exception {
        return wordleDAO.makeGuess(jsonWithIDandGuess,email);
    }

    @Override
    @Transactional
    public List<WordleGame> getAllWonGames(String email) {
        return wordleDAO.getAllWonGames(email);
    }

    @Override
    @Transactional
    public List<WordleGame> getAllLostGames(String email) {
        return wordleDAO.getAllLostGames(email);
    }

    @Override
    @Transactional
    public List<WordleGame> getAllActiveGames(String email) {
        return wordleDAO.getAllActiveGames(email);
    }
}
