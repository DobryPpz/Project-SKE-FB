package com.example.demo.Wordle.dao;

import com.example.demo.CustomUserDetails;
import com.example.demo.Fiszki.models.FlashcardSet;
import com.example.demo.User;
import com.example.demo.Wordle.exceptions.GameAlreadyOverException;
import com.example.demo.Wordle.exceptions.GameDoesNotExistException;
import com.example.demo.Wordle.exceptions.InvalidGuessException;
import com.example.demo.Wordle.models.WordleGame;
import com.example.demo.Wordle.other.WordleWords;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Map;

@Repository
public class WordleDAOImpl implements WordleDAO {
    private List<String> listOfWords = WordleWords.getWords();

    private EntityManager entityManager;

    @Autowired
    public WordleDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public ResponseEntity<?> newGame(FlashcardSet flashcardSet, String side, String user) {
        User userr = entityManager
                .createQuery("FROM User u WHERE u.username = '" + user + "'", User.class)
                .getResultList().get(0);
        WordleGame newGame = new WordleGame(flashcardSet, side, userr);
        userr.addWordleGame(newGame);
        entityManager.persist(newGame);
        return new ResponseEntity<>(newGame, HttpStatus.CREATED);
    }

    @Override
    public List<WordleGame> getAllCurrentGames() {
        List<WordleGame> gamesInSession = entityManager
                .createQuery("FROM WordleGame", WordleGame.class)
                .getResultList();
        return gamesInSession;
    }

    @Override
    public ResponseEntity<?> getGivenGame(String gameID) {
        WordleGame game = entityManager.find(WordleGame.class, gameID);
        if (game == null) return gameDoesntExist(gameID);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> makeGuess(Map<String, String> jsonWithIDandGuess) throws Exception {
        String gameID = jsonWithIDandGuess.get("game");
        String guess = jsonWithIDandGuess.get("guess");
        if (guess.length() == 0) return guessIsNotValid(guess);

        WordleGame game = entityManager.find(WordleGame.class, gameID);

        if (game == null) return gameDoesntExist(gameID);

        switch (game.getStatus()) {
            case ACTIVE:
                break;
            case WON:
            case LOST:
                return gameOver(game);
        }

        checkIfGuessIsRight(game, guess);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @ExceptionHandler(GameAlreadyOverException.class)
    private ResponseEntity<String> gameOver(WordleGame game) {
        String s = "Game is already complete - you " + game.getStatus() + ". The word was: " + game.getWord();
        return new ResponseEntity<>(s, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GameDoesNotExistException.class)
    private ResponseEntity<String> gameDoesntExist(String gameID) {
        String s = "Game with given id " + gameID + " doesn't exist ";
        return new ResponseEntity<>(s, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidGuessException.class)
    private ResponseEntity<String> guessIsNotValid(String guess) {
        String s = "This guess: " + guess + " is not a valid guess.";
        return new ResponseEntity<>(s, HttpStatus.NOT_FOUND);
    }

    public void checkIfGuessIsRight(WordleGame game, String guess) {
        char guess0 = Character.toLowerCase(guess.charAt(0));
        if (game.getWord().contains(String.valueOf(guess0))) {
            game.setGuessedWord(guess0);
        } else {
            game.incIncorrectGuesses();
        }
    }
}
