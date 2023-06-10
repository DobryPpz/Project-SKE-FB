package com.example.demo.Quiz.service;

import com.example.demo.Fiszki.models.FlashcardSet;
import com.example.demo.Hangman.models.HangmanGame;
import com.example.demo.Login.models.User;
import com.example.demo.Quiz.dao.QuizDAO;
import com.example.demo.Quiz.models.QuizGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public class QuizServiceImpl implements QuizService{

    QuizDAO quizDAO;

    @Autowired
    public QuizServiceImpl(QuizDAO quizDAO){
        this.quizDAO = quizDAO;
    }

    @Override
    public ResponseEntity<?> newGame(FlashcardSet flashcardSet, User user) {
        return quizDAO.newGame(flashcardSet,user);
    }

    @Override
    public List<QuizGame> getGamesByUser(User user) {
        return quizDAO.getGamesByUser(user);
    }

    @Override
    public QuizGame findById(int id) {
        return quizDAO.findById(id);
    }

    @Override
    public ResponseEntity<?> makeGuess(QuizGame quizGame, String guess) {
        return quizDAO.makeGuess(quizGame,guess);
    }

}
