package com.example.demo.Hangman.service;


import com.example.demo.Hangman.exceptions.GameAlreadyOverException;
import com.example.demo.Hangman.exceptions.GameDoesNotExistException;
import com.example.demo.Hangman.exceptions.InvalidGuessException;
import com.example.demo.Hangman.models.HangmanGame;
import com.example.demo.Hangman.other.TempClassForWords;
import jakarta.annotation.PostConstruct;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@Service
public class HangmanService {

    private List<String> listOfWords = TempClassForWords.getWords();

    SessionFactory sessionFactory;
    @PostConstruct
    public void init(){
        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(HangmanGame.class)
                .buildSessionFactory();
    }


    public HangmanGame newGame() {
        Session session = sessionFactory.getCurrentSession();

        session.beginTransaction();
        HangmanGame newGame = new HangmanGame(listOfWords);
        session.save(newGame);
        session.getTransaction().commit();
        session.close();
        return newGame;
    }
    public List<HangmanGame> getAllCurrentGames() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        List<HangmanGame> gamesInSession = session.createQuery(
                "SELECT e FROM HangmanGame e",HangmanGame.class).getResultList();
        session.getTransaction().commit();
        session.close();
        return gamesInSession;
    }
    public ResponseEntity<?> getGivenGame(String gameID)  {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        HangmanGame game = session.get(HangmanGame.class,gameID);
        if (game == null) return gameDoesntExist(gameID);
        session.getTransaction().commit();
        session.close();
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    public ResponseEntity<?> makeGuess (Map<String,String> jsonWithIDandGuess) throws Exception {
        String gameID = jsonWithIDandGuess.get("game");
        String guess = jsonWithIDandGuess.get("guess");
        if (guess.length() == 0) return guessIsNotValid(guess);

        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        HangmanGame game = session.get(HangmanGame.class,gameID);

        if (game == null) return gameDoesntExist(gameID);

        switch (game.getStatus()) {
            case ACTIVE:
                break;
            case WON:
            case LOST:
                return gameOver(game);
        }

        checkIfGuessIsRight(game,guess);
        session.getTransaction().commit();
        session.close();
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
