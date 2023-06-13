package com.example.demo.Wordle.dao;

import com.example.demo.Fiszki.models.FlashcardSet;
import com.example.demo.Wordle.exceptions.NotYourGame;
import com.example.demo.Wordle.exceptions.GameAlreadyOverException;
import com.example.demo.Wordle.exceptions.GameDoesNotExistException;
import com.example.demo.Wordle.exceptions.InvalidGuessException;
import com.example.demo.Wordle.models.WordleGame;
import com.example.demo.Wordle.models.WordleGameStatus;
import com.example.demo.Login.models.User;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class WordleDAOImpl implements WordleDAO {
    private EntityManager entityManager;

    @Autowired
    public WordleDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    private User getUserFromEmail(String email) {
        User user = entityManager.
                createQuery("FROM User u WHERE u.email = '" + email + "'", User.class).
                getResultList().get(0);
        return user;
    }

    @Override
    public ResponseEntity<?> newGame(FlashcardSet flashcardSet, String side, String email) {
        User user = getUserFromEmail(email);
        WordleGame newGame = new WordleGame(flashcardSet, side, user);
        user.addWordleGame(newGame);
        entityManager.persist(newGame);
        return new ResponseEntity<>(newGame, HttpStatus.CREATED);
    }

    @Override
    public List<WordleGame> getAllCurrentGames(String email) {
        User user = getUserFromEmail(email);
        var query = entityManager.
                createQuery("FROM WordleGame h WHERE h.user.id = (:userId)", WordleGame.class);
        query.setParameter("userId", user.getId());

        List<WordleGame> games = query.getResultList();

        return games;
    }

    @Override
    public ResponseEntity<?> getGivenGame(String gameID, String email) {
        WordleGame game = entityManager.find(WordleGame.class,gameID);
        if (game == null) return gameDoesntExist(gameID);

        User user = getUserFromEmail(email);

        if (!Objects.equals(user.getId(), game.getUser().getId())) return notYourGame();

        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> makeGuess(Map<String, String> jsonWithIDandGuess, String email) throws Exception {

        String gameID = jsonWithIDandGuess.get("game_id");
        String guess = jsonWithIDandGuess.get("guess");
        if (guess.length() == 0) return guessIsNotValid(guess);

        WordleGame game = entityManager.find(WordleGame.class, gameID);

        User user = getUserFromEmail(email);

        if (!Objects.equals(user.getId(), game.getUser().getId())) return notYourGame();

        if (game == null) return gameDoesntExist(gameID);

        switch (game.getStatus()) {
            case ACTIVE:
                break;
            case WON:
            case LOST:
                return gameOver(game);
        }

        checkGuess(game,guess);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    private List<WordleGame> getAllGamesOfStatus(String email, WordleGameStatus wordleGameStatus) {
        User user = getUserFromEmail(email);
        var query = entityManager.
                createQuery("FROM WordleGame h WHERE h.user.id = (:userId) AND h.status = (:gameStatus)", WordleGame.class);
        query.setParameter("userId", user.getId()).setParameter("gameStatus", wordleGameStatus);

        List<WordleGame> games = query.getResultList();

        return games;
    }

    @Override
    public List<WordleGame> getAllWonGames(String email) {
        return getAllGamesOfStatus(email, WordleGameStatus.WON);
    }

    @Override
    public List<WordleGame> getAllLostGames(String email) {
        return getAllGamesOfStatus(email, WordleGameStatus.LOST);
    }

    @Override
    public List<WordleGame> getAllActiveGames(String email) {
        return getAllGamesOfStatus(email, WordleGameStatus.ACTIVE);
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
        String s = "This guess: " + guess + " is not valid guess";
        return new ResponseEntity<>(s, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotYourGame.class)
    private ResponseEntity<String> notYourGame()
    {
        String s = "This is not your game!";
        return new ResponseEntity<>(s, HttpStatus.FORBIDDEN);
    }

    public void checkGuess(WordleGame game, String guess)
    {
        game.setGuessedWord(guess);
        game.guessesLeft--;
        game.setStatus();
    }
}
