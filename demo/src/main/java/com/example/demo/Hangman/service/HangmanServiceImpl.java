package com.example.demo.Hangman.service;

import com.example.demo.Fiszki.models.FlashcardSet;
import com.example.demo.Hangman.dao.HangmanDAO;
import com.example.demo.Hangman.models.HangmanGame;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class HangmanServiceImpl implements HangmanService{

    private HangmanDAO hangmanDAO;

    @Autowired
    public HangmanServiceImpl(HangmanDAO hangmanDAO) {
        this.hangmanDAO = hangmanDAO;
    }

    @Override
    @Transactional
    public ResponseEntity<?> newGame(FlashcardSet flashcardSet,String side, String email) {
        return hangmanDAO.newGame(flashcardSet,side,email);
    }
    @Override
    @Transactional
    public List<HangmanGame> getAllCurrentGames(String email) {
        return hangmanDAO.getAllCurrentGames(email);
    }
    @Override
    @Transactional
    public ResponseEntity<?> getGivenGame(String gameID,String email)  {
        return hangmanDAO.getGivenGame(gameID,email);
    }

    @Override
    @Transactional
    public ResponseEntity<?> makeGuess (String gameID, Map<String,String> jsonWithGuess,String email) throws Exception {
        return hangmanDAO.makeGuess(gameID,jsonWithGuess,email);
    }

    @Override
    @Transactional
    public List<HangmanGame> getAllWonGames(String email) {
        return hangmanDAO.getAllWonGames(email);
    }

    @Override
    @Transactional
    public List<HangmanGame> getAllLostGames(String email) {
        return hangmanDAO.getAllLostGames(email);
    }

    @Override
    @Transactional
    public List<HangmanGame> getAllActiveGames(String email) {
        return hangmanDAO.getAllActiveGames(email);
    }
}
