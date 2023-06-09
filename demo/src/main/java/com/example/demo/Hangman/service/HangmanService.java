package com.example.demo.Hangman.service;



import com.example.demo.Fiszki.models.FlashcardSet;
import com.example.demo.Hangman.models.HangmanGame;

import org.springframework.http.ResponseEntity;


import java.util.List;
import java.util.Map;


public interface HangmanService {

    public ResponseEntity<?> newGame(FlashcardSet flashcardSet,String side, String email);

    public List<HangmanGame> getAllCurrentGames(String email);

    public ResponseEntity<?> getGivenGame(String gameID,String email);

    public ResponseEntity<?> makeGuess(String gameId,Map<String, String> jsonWithGuess,String email) throws Exception;

    List<HangmanGame> getAllWonGames(String emailOfAuthenticatedUser);

    List<HangmanGame> getAllLostGames(String emailOfAuthenticatedUser);

    List<HangmanGame> getAllActiveGames(String emailOfAuthenticatedUser);
}
