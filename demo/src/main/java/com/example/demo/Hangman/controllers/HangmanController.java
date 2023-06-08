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

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
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
    public ResponseEntity<?> getAllFlashcardSetsOfUserToChooseFrom() {
        String email = getEmailOfAuthenticatedUser();

        List<FlashcardSet> flashcardSets = flashcardSetService.findAllByUser(email);
        return new ResponseEntity<>(flashcardSets, HttpStatus.OK);

    }

    @PostMapping(value = "/new")
    public ResponseEntity<?> newGame(@RequestBody Map<String,String> flashcardSetInfo) {
        String email = getEmailOfAuthenticatedUser();
        System.out.println(email);

        int flashcardSetId = Integer.parseInt(flashcardSetInfo.get("set_id"));
        FlashcardSet flashcardSet = flashcardSetService.findById(flashcardSetId);

        String side = flashcardSetInfo.get("side");

        return hangmanService.newGame(flashcardSet,side,email);
    }

    @GetMapping("/games")
    public List<HangmanGame> getAllGames() {

        return hangmanService.getAllCurrentGames(getEmailOfAuthenticatedUser());
    }
    @GetMapping("/games/won")
    public List<HangmanGame> getAllWonGames() {

        return hangmanService.getAllWonGames(getEmailOfAuthenticatedUser());
    }
    @GetMapping("/games/lost")
    public List<HangmanGame> getAllLostGames() {

        return hangmanService.getAllLostGames(getEmailOfAuthenticatedUser());
    }
    @GetMapping("/games/active")
    public List<HangmanGame> getAllActiveGames() {

        return hangmanService.getAllActiveGames(getEmailOfAuthenticatedUser());
    }
    @GetMapping(path = "/games/{gameID}")
    public ResponseEntity<?> getGivenGame(@PathVariable String gameID)  {
        return hangmanService.getGivenGame(gameID,getEmailOfAuthenticatedUser());
    }
    @PutMapping(value = "/guess", headers="Accept=application/json", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> makeGuess (@RequestBody Map<String,String> jsonWithIDandGuess) throws Exception {
        return hangmanService.makeGuess(jsonWithIDandGuess,getEmailOfAuthenticatedUser());
    }

    public String getEmailOfAuthenticatedUser()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return email;
    }

}
