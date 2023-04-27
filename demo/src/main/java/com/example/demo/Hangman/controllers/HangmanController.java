package com.example.demo.Hangman.controllers;
import com.example.demo.Hangman.exceptions.GameAlreadyOverException;
import com.example.demo.Hangman.exceptions.GameDoesNotExistException;
import com.example.demo.Hangman.exceptions.InvalidGuessException;
import com.example.demo.Hangman.models.HangmanGame;
import com.example.demo.Hangman.other.TempClassForWords;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



@RestController
class HangmanController {
    private List<String> listOfWords = TempClassForWords.getWords();
    private @Autowired ServletContext servletContext;
    private int idOfGame = 0;

    @RequestMapping(value = "/hangman/new_game", method = RequestMethod.GET)
    public HangmanGame newGame(HttpSession session) {
        idOfGame++;
        HangmanGame newGame = new HangmanGame(listOfWords,idOfGame);
        List<HangmanGame> games = getAllCurrentGames(session);
        games.add(newGame);
        return newGame;
    }

    @RequestMapping("/hangman/current_games")
    private List<HangmanGame> getAllCurrentGames(HttpSession session) {
        List<HangmanGame> gamesInSession = (List<HangmanGame>) session.getAttribute("gamesInSession");
        if (gamesInSession == null) {
            gamesInSession = new ArrayList<>();
            session.setAttribute("gamesInSession", gamesInSession);
        }
        return gamesInSession;
    }
    @RequestMapping(value = "/hangman/guess", method = RequestMethod.POST, headers="Accept=application/json", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> makeGuess (@RequestBody Map<String,String> json, HttpSession session) throws Exception {
        String gameID = json.get("game");
        String guess = json.get("guess");
        if (guess.length() == 0) return guessIsNotValid(guess);

        HangmanGame game = getGame(gameID, session);
        if (game == null) return gameDoesntExist(gameID);

        switch (game.getStatus()) {
            case ACTIVE:
                break;
            case WON:
                return gameOver(game);
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

    private HangmanGame getGame(String id, HttpSession session) {
        List<HangmanGame> games = getAllCurrentGames(session);
        for (HangmanGame game : games) {
            if (String.valueOf(game.getId()).equals(id)) {
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
