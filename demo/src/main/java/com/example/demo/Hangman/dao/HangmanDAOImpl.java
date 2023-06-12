package com.example.demo.Hangman.dao;

import com.example.demo.Fiszki.models.FlashcardSet;
import com.example.demo.Hangman.exceptions.GameAlreadyOverException;
import com.example.demo.Hangman.exceptions.GameDoesNotExistException;
import com.example.demo.Hangman.exceptions.InvalidGuessException;
import com.example.demo.Hangman.exceptions.NotYourGame;
import com.example.demo.Hangman.models.HangmanGame;
import com.example.demo.Hangman.models.HangmanGameStatus;
import com.example.demo.Login.models.User;
import jakarta.persistence.EntityManager;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class HangmanDAOImpl implements HangmanDAO {
    //private List<String> listOfWords = TempClassForWords.getWords();


    private EntityManager entityManager;

    @Autowired
    public HangmanDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    private User getUserFromEmail(String email)
    {
        User user = entityManager.
                createQuery("FROM User u WHERE u.email = '"+email+"'", User.class).
                getResultList().get(0);
        return user;
    }

    @Override
    public ResponseEntity<?> newGame(FlashcardSet flashcardSet,String side,String email) {
        User user = getUserFromEmail(email);
        HangmanGame newGame = new HangmanGame(flashcardSet,side,user);
        user.addHangmanGame(newGame);
        entityManager.persist(newGame);
        return new ResponseEntity<>(newGame, HttpStatus.CREATED);
    }
    @Override
    public List<HangmanGame> getAllCurrentGames(String email) {
        User user = getUserFromEmail(email);
        var query = entityManager.
                        createQuery("FROM HangmanGame h WHERE h.user.id = (:userId)",HangmanGame.class);
        query.setParameter("userId",user.getId());

        List<HangmanGame> games = query.getResultList();

        return games;
    }
    @Override
    public ResponseEntity<?> getGivenGame(String gameID,String email)  {
        HangmanGame game = entityManager.find(HangmanGame.class,gameID);
        if (game == null) return gameDoesntExist(gameID);

        User user = getUserFromEmail(email);

        if (!Objects.equals(user.getId(), game.getUser().getId())) return notYourGame();

        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> makeGuess (String gameID, Map<String, String> jsonWithGuess, String email) throws Exception {

        //String gameID = jsonWithGuess.get("game_id");
        String guess = jsonWithGuess.get("guess");
        if (guess.length() == 0) return guessIsNotValid(guess);

        HangmanGame game = entityManager.find(HangmanGame.class,gameID);

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

        checkIfGuessIsRight(game,guess);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    private List<HangmanGame> getAllGamesOfStatus(String email,HangmanGameStatus hangmanGameStatus)
    {
        User user = getUserFromEmail(email);
        var query = entityManager.
                createQuery("FROM HangmanGame h WHERE h.user.id = (:userId) AND h.status = (:gameStatus)",HangmanGame.class);
        query.setParameter("userId",user.getId()).setParameter("gameStatus",hangmanGameStatus);

        List<HangmanGame> games = query.getResultList();

        return games;
    }
    @Override
    public List<HangmanGame> getAllWonGames(String email) {
        return getAllGamesOfStatus(email,HangmanGameStatus.WON);
    }

    @Override
    public List<HangmanGame> getAllLostGames(String email) {
        return getAllGamesOfStatus(email,HangmanGameStatus.LOST);
    }

    @Override
    public List<HangmanGame> getAllActiveGames(String email) {
        return getAllGamesOfStatus(email,HangmanGameStatus.ACTIVE);
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
    @ExceptionHandler(NotYourGame.class)
    private ResponseEntity<String> notYourGame()
    {
        String s = "This is not your game!";
        return new ResponseEntity<>(s, HttpStatus.FORBIDDEN);
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
