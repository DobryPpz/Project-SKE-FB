package com.example.demo.Quiz.dao;

import com.example.demo.Fiszki.models.FlashcardSet;
import com.example.demo.Login.models.User;
import com.example.demo.Quiz.dto.QuizGameDTO;
import com.example.demo.Quiz.models.QuizGame;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class QuizDAOImpl implements QuizDAO{

    private EntityManager entityManager;

    @Autowired
    public QuizDAOImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public ResponseEntity<?> newGame(FlashcardSet flashcardSet, User user) {
        QuizGame newGame = new QuizGame(flashcardSet,user);
        user.addQuizGame(newGame);
        entityManager.persist(newGame);
        return new ResponseEntity<>(new QuizGameDTO("game started",0,newGame.getCurrentFront()),
                HttpStatus.CREATED);
    }

    @Override
    public List<QuizGame> getGamesByUser(User user) {
        TypedQuery<QuizGame> currentGames = entityManager.createQuery(
                "FROM QuizGame WHERE user=:user",QuizGame.class);
        currentGames.setParameter("user",user);
        List<QuizGame> results = currentGames.getResultList();
        return results;
    }

    @Override
    public QuizGame findById(int id) {
        return entityManager.find(QuizGame.class,id);
    }

    @Override
    @Transactional
    public ResponseEntity<?> makeGuess(QuizGame quizGame, String guess) {
        QuizGameDTO response = quizGame.check(guess);
        entityManager.persist(quizGame);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
