package com.example.demo.Hangman.service;


import com.example.demo.Hangman.dao.HangmanDAO;
import com.example.demo.Hangman.models.HangmanGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


public interface HangmanService {

    public ResponseEntity<?> newGame();

    public List<HangmanGame> getAllCurrentGames();

    public ResponseEntity<?> getGivenGame(String gameID);

    public ResponseEntity<?> makeGuess(Map<String, String> jsonWithIDandGuess) throws Exception;

}
