package com.example.demo.Hangman.service;


import com.example.demo.CustomUserDetails;
import com.example.demo.Fiszki.models.FlashcardSet;
import com.example.demo.Hangman.dao.HangmanDAO;
import com.example.demo.Hangman.models.HangmanGame;
import com.example.demo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


public interface HangmanService {

    public ResponseEntity<?> newGame(List<FlashcardSet> flashcardSets, String user);

    public List<HangmanGame> getAllCurrentGames();

    public ResponseEntity<?> getGivenGame(String gameID);

    public ResponseEntity<?> makeGuess(Map<String, String> jsonWithIDandGuess) throws Exception;

}
