package com.example.demo.Hangman.dao;
import com.example.demo.CustomUserDetails;
import com.example.demo.Hangman.models.HangmanGame;
import com.example.demo.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;
import java.util.Map;

public interface HangmanDAO {

    public ResponseEntity<?> newGame(String user);

    public List<HangmanGame> getAllCurrentGames();

    public ResponseEntity<?> getGivenGame(String gameID);

    public ResponseEntity<?> makeGuess(Map<String, String> jsonWithIDandGuess) throws Exception;


}
