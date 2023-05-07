package com.example.demo.Wordle.controllers;

import com.example.demo.Wordle.exceptions.GameAlreadyOverException;
import com.example.demo.Wordle.exceptions.GameDoesNotExistException;
import com.example.demo.Wordle.exceptions.InvalidGuessException;
import com.example.demo.Wordle.models.WordleGame;
import com.example.demo.Wordle.other.TempClassForWords;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
class WordleController {

    private List<String> listOfWords = TempClassForWords.getWords();
    private @Autowired ServletContext servletContext;
    private int idOfGame = 0;

    @RequestMapping(value = "/wordle/new_game", method = RequestMethod.GET)
    public WordleGame newGame(HttpSession session) {
        idOfGame++;
        WordleGame newGame = new WordleGame(listOfWords,idOfGame);
        List<WordleGame> games = getAllCurrentGames(session);
        games.add(newGame);
        return newGame;
    }

    @RequestMapping("/wordle/current_games")
    private List<WordleGame> getAllCurrentGames(HttpSession session) {
        List<WordleGame> gamesInSession = (List<WordleGame>) session.getAttribute("gamesInSession");
        if (gamesInSession == null) {
            gamesInSession = new ArrayList<>();
            session.setAttribute("gamesInSession", gamesInSession);
        }
        return gamesInSession;
    }

    @RequestMapping(value = "/wordle/guess", method = RequestMethod.POST, headers="Accept=application/json", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> makeGuess (@RequestBody Map<String,String> json, HttpSession session) throws Exception {
        String gameID = json.get("game");
        String guess = json.get("guess");

        if (guess == null || guess.isEmpty() || guess.length() != 5) {
            throw new InvalidGuessException(guess);
        }

        WordleGame game = getGame(gameID, session);
        if (game == null) {
            throw new GameDoesNotExistException(gameID);
        }

        switch (game.getStatus()) {
            case ACTIVE:
                break;
            case WON:
                throw new GameAlreadyOverException(game);
            case LOST:
                throw new GameAlreadyOverException(game);
        }

        game.makeGuess(guess);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @ExceptionHandler(GameAlreadyOverException.class)
    private ResponseEntity<String> gameOver(GameAlreadyOverException ex)
    {
        String s = "Game is already complete - you "+ex.getGame().getStatus()+". The word was: "+ex.getGame().getWord();
        return new ResponseEntity<>(s, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GameDoesNotExistException.class)
    private ResponseEntity<String> gameDoesntExist(GameDoesNotExistException ex)
    {
        String s = "Game with given id "+ex.getGameID()+" doesn't exist ";
        return new ResponseEntity<>(s, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidGuessException.class)
    private ResponseEntity<String> guessIsNotValid(InvalidGuessException ex) {
        String s = "This guess: " + ex.getGuess() + " is not valid guess";
        return new ResponseEntity<>(s, HttpStatus.NOT_FOUND);
    }

    private WordleGame getGame(String id, HttpSession session) {
        List<WordleGame> games = getAllCurrentGames(session);
        for (WordleGame game : games) {
            if (String.valueOf(game.getId()).equals(id)){
                return game;
            }
        }
        return null;
    }

    public void checkIfGuessIsRight(HangmanGame game,String guess)
    {
        char guess0 = Character.toLowerCase(guess.charAt(0));
        if (game.getWord().contains(String.valueOf(guess0))){
            game.setGuessedWord(guess0);
        }
        else game.incIncorrect_guesses();
    }
}