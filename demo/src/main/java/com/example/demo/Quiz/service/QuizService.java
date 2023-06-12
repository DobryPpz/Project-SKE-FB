package com.example.demo.Quiz.service;

import com.example.demo.Fiszki.models.FlashcardSet;
import com.example.demo.Hangman.models.HangmanGame;
import com.example.demo.Login.models.User;
import com.example.demo.Quiz.models.QuizGame;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface QuizService {
    public ResponseEntity<?> newGame(FlashcardSet flashcardSet, User user);

    public List<QuizGame> getGamesByUser(User user);
    public QuizGame findById(int id);
    public ResponseEntity<?> makeGuess(QuizGame quizGame, String guess);
}
