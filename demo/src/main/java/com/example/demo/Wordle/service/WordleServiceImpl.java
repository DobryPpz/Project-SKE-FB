package com.example.demo.Wordle.service;

import com.example.demo.Wordle.dao.WordleDAO;
import com.example.demo.Wordle.models.WordleGame;
import com.example.demo.Fiszki.models.FlashcardSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class WordleServiceImpl implements WordleService {
    private WordleDAO wordleDAO;

    @Autowired
    public WordleServiceImpl(WordleDAO wordleDAO) {
        this.wordleDAO = wordleDAO;
    }

    @Override
    public ResponseEntity<?> newGame(FlashcardSet flashcardSet, String side, String user) {
        return wordleDAO.newGame(flashcardSet, side, user);
    }

    @Override
    public List<WordleGame> getAllCurrentGames() {
        return wordleDAO.getAllCurrentGames();
    }

    @Override
    public ResponseEntity<?> getGivenGame(String gameID) {
        return wordleDAO.getGivenGame(gameID);
    }

    @Override
    public ResponseEntity<?> makeGuess(Map<String, String> jsonWithIDandGuess) throws Exception {
        return wordleDAO.makeGuess(jsonWithIDandGuess);
    }
}
