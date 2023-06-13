package com.example.demo.Quiz.dao;

import com.example.demo.Fiszki.models.FlashcardSet;
import com.example.demo.Hangman.models.HangmanGame;
import com.example.demo.Login.models.User;
import com.example.demo.Quiz.models.QuizGame;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

public interface QuizDAO {
    public ResponseEntity<?> newGame(FlashcardSet flashcardSet, User user);

    public List<QuizGame> getGamesByUser(User user);

    public QuizGame findById(int id);

    public ResponseEntity<?> makeGuess(QuizGame quizGame, String guess);
}
