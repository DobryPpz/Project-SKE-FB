package com.example.demo.Wordle.controllers;

import com.example.demo.Fiszki.models.FlashcardSet;
import com.example.demo.Fiszki.service.FlashcardSetService;
import com.example.demo.Wordle.models.WordleGame;
import com.example.demo.Wordle.service.WordleService;
import com.example.demo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

import java.util.List;
import java.util.Map;

@RestController
class WordleController {
    private WordleService wordleService;
    private FlashcardSetService flashcardSetService;

    @Autowired
    public WordleController(WordleService wordleService, FlashcardSetService flashcardSetService) {
        this.wordleService = wordleService;
        this.flashcardSetService = flashcardSetService;
    }

    @GetMapping(value = "/wordle/new_game")
    public ResponseEntity<?> getAllFlashcardSetsOfUserToChooseFrom() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        List<FlashcardSet> flashcardSets = flashcardSetService.findAllByUser(username);
        return new ResponseEntity<>(flashcardSets, HttpStatus.OK);
    }

    @PostMapping(value = "/wordle/new_game")
    public ResponseEntity<?> newGame(@RequestBody Map<String, String> flashcardSetInfo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        int flashcardSetId = Integer.parseInt(flashcardSetInfo.get("set_id"));
        FlashcardSet flashcardSet = flashcardSetService.findById(flashcardSetId);

        String side = flashcardSetInfo.get("side");

        return wordleService.newGame(flashcardSet, side, username);
    }

    @GetMapping("/wordle/current_games")
    public List<WordleGame> getAllCurrentGames() {
        return wordleService.getAllCurrentGames();
    }

    @GetMapping(path = "/wordle/current_games/{gameID}")
    public ResponseEntity<?> getGivenGame(@PathVariable String gameID) {
        return wordleService.getGivenGame(gameID);
    }

    @PostMapping(value = "/wordle/guess", headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> makeGuess(@RequestBody Map<String, String> jsonWithIDandGuess) throws Exception {
        return wordleService.makeGuess(jsonWithIDandGuess);
    }
}
