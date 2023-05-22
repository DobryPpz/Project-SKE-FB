package com.example.demo.Hangman.dao;

import com.example.demo.Hangman.exceptions.GameAlreadyOverException;
import com.example.demo.Hangman.exceptions.GameDoesNotExistException;
import com.example.demo.Hangman.exceptions.InvalidGuessException;
import com.example.demo.Hangman.models.HangmanGame;
import com.example.demo.Hangman.other.TempClassForWords;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Map;

@Repository
public class HangmanDAOImpl implements HangmanDAO {
    private List<String> listOfWords = TempClassForWords.getWords();


    private EntityManager entityManager;

    @Autowired
    public HangmanDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public ResponseEntity<?> newGame() {
        HangmanGame newGame = new HangmanGame(listOfWords);
        entityManager.persist(newGame);
        return new ResponseEntity<>(newGame, HttpStatus.CREATED);
    }
    @Override
    public List<HangmanGame> getAllCurrentGames() {
        List<HangmanGame> gamesInSession = entityManager.
                        createQuery("FROM HangmanGame",HangmanGame.class).
                        getResultList();
        return gamesInSession;
    }
    @Override
    public ResponseEntity<?> getGivenGame(String gameID)  {
        HangmanGame game = entityManager.find(HangmanGame.class,gameID);
        if (game == null) return gameDoesntExist(gameID);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> makeGuess (Map<String,String> jsonWithIDandGuess) throws Exception {
        String gameID = jsonWithIDandGuess.get("game");
        String guess = jsonWithIDandGuess.get("guess");
        if (guess.length() == 0) return guessIsNotValid(guess);

        HangmanGame game = entityManager.find(HangmanGame.class,gameID);

        if (game == null) return gameDoesntExist(gameID);

        switch (game.getStatus()) {
            case ACTIVE:
                break;
            case WON:
            case LOST:
                return gameOver(game);
        }

        checkIfGuessIsRight(game,guess);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @ExceptionHandler(GameAlreadyOverException.class)
    private ResponseEntity<String> gameOver(HangmanGame game)
    {
        String s = "Game is already complete - you "+game.getStatus()+". The word was: "+game.getWord();
        return new ResponseEntity<>(s, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(GameDoesNotExistException.class)
    private ResponseEntity<String> gameDoesntExist(String gameID)
    {
        String s = "Game with given id "+gameID+" doesn't exist ";
        return new ResponseEntity<>(s, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(InvalidGuessException.class)
    private ResponseEntity<String> guessIsNotValid(String guess)
    {
        String s = "This guess: "+guess+" is not valid guess xd ";
        return new ResponseEntity<>(s, HttpStatus.NOT_FOUND);
    }
    public void checkIfGuessIsRight(HangmanGame game,String guess)
    {
        char guess0 = Character.toLowerCase(guess.charAt(0));
        if (game.getWord().contains(String.valueOf(guess0))){
            game.setGuessedWord(guess0);
        }
        else game.incIncorrectGuesses();
    }
}