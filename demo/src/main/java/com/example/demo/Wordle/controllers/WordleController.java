package com.example.demo.Wordle.controllers;

import com.example.demo.Wordle.exceptions.GameAlreadyOverException;
import com.example.demo.Wordle.exceptions.GameDoesNotExistException;
import com.example.demo.Wordle.exceptions.InvalidGuessException;
import com.example.demo.Wordle.models.WordleGame;
import com.example.demo.Wordle.models.WordleGameStatus;
import com.example.demo.Wordle.other.WordleWords;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
class WordleController {

    private List<String> listOfWords = WordleWords.getWords();
    private @Autowired ServletContext servletContext;
    private int idOfGame = 0;

    @RequestMapping(value = "/wordle/new_game", method = RequestMethod.GET)
    public WordleGame newGame(HttpSession session) {
        idOfGame++;
        WordleGame newGame = new WordleGame(listOfWords, idOfGame);
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

    @RequestMapping(value = "/wordle/guess", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> makeGuess(@RequestBody Map<String, String> json, HttpSession session) throws Exception {
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
                return gameOver(game);
            case LOST:
                return gameOver(game);
        }

        game.makeGuess(guess);

        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @ExceptionHandler(GameAlreadyOverException.class)
    private ResponseEntity<String> gameOver(WordleGame game) {
        String s = "Game is already complete - you " + game.getStatus() + ". The word was: " + game.getWord();
        return new ResponseEntity<>(s, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GameDoesNotExistException.class)
    private ResponseEntity<String> gameDoesntExist(WordleGame game) {
        String s = "Game with given id " + game.getGameID() + " doesn't exist ";
        return new ResponseEntity<>(s, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidGuessException.class)
    private ResponseEntity<String> guessIsNotValid(WordleGame game) {
        String s = "This guess: " + game.getGuess() + " is not a valid guess";
        return new ResponseEntity<>(s, HttpStatus.NOT_FOUND);
    }

    private WordleGame getGame(String id, HttpSession session) {
        List<WordleGame> games = getAllCurrentGames(session);
        for (WordleGame game : games) {
            if (String.valueOf(game.getGameID()).equals(id)) {
                return game;
            }
        }
        return null;
    }

    private int calculateGuessesLeft(WordleGame game) {
        return game.getMaxGuessCount() - game.getGuessCount();
    }
}
