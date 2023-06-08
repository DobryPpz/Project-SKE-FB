package com.example.demo.Wordle.service;

import com.example.demo.Fiszki.models.FlashcardSet;
import com.example.demo.Wordle.models.WordleGame;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface WordleService{

    public ResponseEntity<?> newGame(FlashcardSet flashcardSet, String side, String email);

    public List<WordleGame> getAllCurrentGames(String email);

    public ResponseEntity<?> getGivenGame(String gameID,String email);

    public ResponseEntity<?> makeGuess(Map<String, String> jsonWithIDandGuess, String email) throws Exception;

    List<WordleGame> getAllWonGames(String emailOfAuthenticatedUser);

    List<WordleGame> getAllLostGames(String emailOfAuthenticatedUser);

    List<WordleGame> getAllActiveGames(String emailOfAuthenticatedUser);
}
