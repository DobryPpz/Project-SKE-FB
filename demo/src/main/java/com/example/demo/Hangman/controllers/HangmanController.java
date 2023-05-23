package com.example.demo.Hangman.controllers;
import com.example.demo.Fiszki.service.FlashcardSetService;
import com.example.demo.Hangman.models.HangmanGame;
import com.example.demo.Hangman.service.HangmanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import java.util.List;
import java.util.Map;

@RestController

class HangmanController {
    private HangmanService hangmanService;
    private FlashcardSetService flashcardSetService;
    @Autowired
    public HangmanController(HangmanService hangmanService, FlashcardSetService flashcardSetService) {
        this.hangmanService = hangmanService;
        this.flashcardSetService = flashcardSetService;
    }

    @GetMapping(value = "/hangman/new_game")
    public ResponseEntity<?> newGame() {
        return hangmanService.newGame();
    }

    @GetMapping("/hangman/current_games")
    public List<HangmanGame> getAllCurrentGames() {

        return hangmanService.getAllCurrentGames();
    }
    @GetMapping(path = "/hangman/current_games/{gameID}")
    public ResponseEntity<?> getGivenGame(@PathVariable String gameID)  {
        return hangmanService.getGivenGame(gameID);
    }
    @PostMapping(value = "/hangman/guess", headers="Accept=application/json", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> makeGuess (@RequestBody Map<String,String> jsonWithIDandGuess) throws Exception {
        return hangmanService.makeGuess(jsonWithIDandGuess);
    }

}
