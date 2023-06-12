package com.example.demo.Hangman.controllers;
import com.example.demo.Fiszki.models.FlashcardSet;
import com.example.demo.Fiszki.service.FlashcardSetService;
import com.example.demo.Hangman.models.HangmanGame;
import com.example.demo.Hangman.service.HangmanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@ApiModel(description = "hangman controller")
@RequestMapping("/user/hangman")
class HangmanController {
    private HangmanService hangmanService;
    private FlashcardSetService flashcardSetService;
    @Autowired
    public HangmanController(HangmanService hangmanService, FlashcardSetService flashcardSetService) {
        this.hangmanService = hangmanService;
        this.flashcardSetService = flashcardSetService;
    }

    @GetMapping(value = "/new")
    @ApiOperation(value = "get all flashcard sets of user to choose from before the game")
    public ResponseEntity<?> getAllFlashcardSetsOfUserToChooseFrom() {
        String email = getEmailOfAuthenticatedUser();

        List<FlashcardSet> flashcardSets = flashcardSetService.findAllByUser(email);
        return new ResponseEntity<>(flashcardSets, HttpStatus.OK);

    }

    @PostMapping(value = "/new")
    @ApiOperation(value = "start a new hangman game with selected set")
    public ResponseEntity<?> newGame(@RequestBody Map<String,String> flashcardSetInfo) {
        String email = getEmailOfAuthenticatedUser();
        System.out.println(email);

        int flashcardSetId = Integer.parseInt(flashcardSetInfo.get("set_id"));
        FlashcardSet flashcardSet = flashcardSetService.findById(flashcardSetId);

        String side = flashcardSetInfo.get("side");

        return hangmanService.newGame(flashcardSet,side,email);
    }

    @GetMapping("/games")
    @ApiOperation(value = "get all hangman games for user")
    public List<HangmanGame> getAllGames() {

        return hangmanService.getAllCurrentGames(getEmailOfAuthenticatedUser());
    }
    @GetMapping("/games/won")
    @ApiOperation(value = "get all won hangman games for user")
    public List<HangmanGame> getAllWonGames() {

        return hangmanService.getAllWonGames(getEmailOfAuthenticatedUser());
    }
    @GetMapping("/games/lost")
    @ApiOperation(value = "get all lost hangman games for user")
    public List<HangmanGame> getAllLostGames() {

        return hangmanService.getAllLostGames(getEmailOfAuthenticatedUser());
    }
    @GetMapping("/games/active")
    @ApiOperation(value = "get all active hangman games for user")
    public List<HangmanGame> getAllActiveGames() {

        return hangmanService.getAllActiveGames(getEmailOfAuthenticatedUser());
    }
    @GetMapping(path = "/games/{gameID}")
    @ApiOperation(value = "get given game")
    public ResponseEntity<?> getGivenGame(@PathVariable String gameID)  {
        return hangmanService.getGivenGame(gameID,getEmailOfAuthenticatedUser());
    }
    @PutMapping(value = "/guess/{gameID}", headers="Accept=application/json", consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "guess letter in hangman game")
    public ResponseEntity<?> makeGuess (@PathVariable String gameID, @RequestBody Map<String,String> jsonWithGuess) throws Exception {
        return hangmanService.makeGuess(gameID,jsonWithGuess,getEmailOfAuthenticatedUser());
    }

    public String getEmailOfAuthenticatedUser()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return email;
    }

}
