package com.example.demo.Wordle.controllers;

import com.example.demo.Fiszki.models.FlashcardSet;
import com.example.demo.Fiszki.service.FlashcardSetService;
import com.example.demo.Wordle.models.WordleGame;
import com.example.demo.Wordle.service.WordleService;
import com.example.demo.Wordle.exceptions.GameAlreadyOverException;
import com.example.demo.Wordle.exceptions.GameDoesNotExistException;
import com.example.demo.Wordle.exceptions.InvalidGuessException;
import com.example.demo.Wordle.models.WordleGameStatus;
import com.example.demo.Wordle.other.WordleWords;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user/wordle")
class WordleController {
    private WordleService wordleService;
    private FlashcardSetService flashcardSetService;

    @Autowired
    public WordleController(WordleService wordleService, FlashcardSetService flashcardSetService) {
        this.wordleService = wordleService;
        this.flashcardSetService = flashcardSetService;
    }

    @GetMapping(value = "/new")
    public ResponseEntity<?> getAllFlashcardSetsOfUserToChooseFrom() {
        String email = getEmailOfAuthenticatedUser();

        List<FlashcardSet> flashcardSets = flashcardSetService.findAllByUser(email);
        return new ResponseEntity<>(flashcardSets, HttpStatus.OK);

    }

    @PostMapping(value = "/new")
    public ResponseEntity<?> newGame(@RequestBody Map<String, String> flashcardSetInfo) {
        String email = getEmailOfAuthenticatedUser();
        System.out.println(email);

        int flashcardSetId = Integer.parseInt(flashcardSetInfo.get("set_id"));
        FlashcardSet flashcardSet = flashcardSetService.findById(flashcardSetId);

        String side = flashcardSetInfo.get("side");

        return wordleService.newGame(flashcardSet, side, email);
    }

    @GetMapping("/games")
    public List<WordleGame> getAllGames() {

        return wordleService.getAllCurrentGames(getEmailOfAuthenticatedUser());
    }

    @GetMapping("/games/won")
    public List<WordleGame> getAllWonGames() {

        return wordleService.getAllWonGames(getEmailOfAuthenticatedUser());
    }

    @GetMapping("/games/lost")
    public List<WordleGame> getAllLostGames() {

        return wordleService.getAllLostGames(getEmailOfAuthenticatedUser());
    }

    @GetMapping("/games/active")
    public List<WordleGame> getAllActiveGames() {

        return wordleService.getAllActiveGames(getEmailOfAuthenticatedUser());
    }

    @GetMapping(path = "/games/{gameID}")
    public ResponseEntity<?> getGivenGame(@PathVariable String gameID) {
        return wordleService.getGivenGame(gameID, getEmailOfAuthenticatedUser());
    }

    @PutMapping(value = "/guess", headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> makeGuess(@RequestBody Map<String, String> jsonWithIDandGuess) throws Exception {
        return wordleService.makeGuess(jsonWithIDandGuess, getEmailOfAuthenticatedUser());
    }

    public String getEmailOfAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return email;
    }

}