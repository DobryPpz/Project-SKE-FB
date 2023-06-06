package com.example.demo.Hangman.dao;
import com.example.demo.Fiszki.models.FlashcardSet;
import com.example.demo.Hangman.models.HangmanGame;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;
import java.util.Map;

public interface HangmanDAO {

    public ResponseEntity<?> newGame(FlashcardSet flashcardSet,String side,String email);

    public List<HangmanGame> getAllCurrentGames(String email);

    public ResponseEntity<?> getGivenGame(String gameID,String email);

    public ResponseEntity<?> makeGuess(Map<String, String> jsonWithIDandGuess,String email) throws Exception;


    List<HangmanGame> getAllWonGames(String email);

    List<HangmanGame> getAllLostGames(String email);

    List<HangmanGame> getAllActiveGames(String email);
}
