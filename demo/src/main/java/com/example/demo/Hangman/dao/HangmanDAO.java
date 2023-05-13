package com.example.demo.Hangman.dao;
import com.example.demo.Hangman.models.HangmanGame;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface HangmanDAO {

    public ResponseEntity<?> newGame();

    public List<HangmanGame> getAllCurrentGames();

    public ResponseEntity<?> getGivenGame(String gameID);

    public ResponseEntity<?> makeGuess(Map<String, String> jsonWithIDandGuess) throws Exception;


}
