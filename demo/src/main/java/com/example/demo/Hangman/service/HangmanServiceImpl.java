package com.example.demo.Hangman.service;

import com.example.demo.CustomUserDetails;
import com.example.demo.Hangman.dao.HangmanDAO;
import com.example.demo.Hangman.models.HangmanGame;
import com.example.demo.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<?> newGame(String user) {

        return hangmanDAO.newGame(user);
    }
    @Override
    @Transactional
    public List<HangmanGame> getAllCurrentGames() {
        return hangmanDAO.getAllCurrentGames();
    }
    @Override
    @Transactional
    public ResponseEntity<?> getGivenGame(String gameID)  {
        return hangmanDAO.getGivenGame(gameID);
    }

    @Override
    @Transactional
    public ResponseEntity<?> makeGuess (Map<String,String> jsonWithIDandGuess) throws Exception {
        return hangmanDAO.makeGuess(jsonWithIDandGuess);
    }
}
