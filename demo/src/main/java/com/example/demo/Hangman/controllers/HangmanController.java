package com.example.demo.Hangman.controllers;
import com.example.demo.Fiszki.models.FlashcardSet;
import com.example.demo.Fiszki.service.FlashcardSetService;
import com.example.demo.Hangman.models.HangmanGame;
import com.example.demo.Hangman.service.HangmanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @GetMapping(value = "/hangman/new")
    public ResponseEntity<?> getAllFlashcardSetsOfUserToChooseFrom() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        List<FlashcardSet> flashcardSets = flashcardSetService.findAllByUser(username);
        return new ResponseEntity<>(flashcardSets, HttpStatus.OK);

    }

    @PostMapping(value = "/hangman/new")
    public ResponseEntity<?> newGame(@RequestBody Map<String,String> flashcardSetInfo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        int flashcardSetId = Integer.parseInt(flashcardSetInfo.get("set_id"));
        FlashcardSet flashcardSet = flashcardSetService.findById(flashcardSetId);

        String side = flashcardSetInfo.get("side");

        return hangmanService.newGame(flashcardSet,side,username);
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
