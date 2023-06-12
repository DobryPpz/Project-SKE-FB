package com.example.demo.Quiz.controllers;

import com.example.demo.Fiszki.models.FlashcardSet;
import com.example.demo.Fiszki.service.FlashcardSetService;
import com.example.demo.Login.models.User;
import com.example.demo.Login.repository.UserRepository;
import com.example.demo.Quiz.models.QuizGame;
import com.example.demo.Quiz.service.QuizService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@ApiModel(description = "quiz controller")
@RequestMapping("/user/quiz")
public class QuizController {
    QuizService quizService;
    FlashcardSetService flashcardSetService;
    private UserRepository userRepository;
    @Autowired
    public QuizController(QuizService quizService,
                          FlashcardSetService flashcardSetService,
                          UserRepository userRepository){
        this.quizService = quizService;
        this.flashcardSetService = flashcardSetService;
        this.userRepository = userRepository;
    }
    @GetMapping("/new")
    @ApiOperation(value = "get all flashcard sets of user to choose from")
    public ResponseEntity<?> getAllFlashcardSetsOfUserToChooseFrom(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<FlashcardSet> flashcardSets = flashcardSetService.findAllByUser(username);
        return new ResponseEntity<>(flashcardSets, HttpStatus.OK);
    }

    @PostMapping("/new")
    @ApiOperation(value = "start a new quiz game with selected set")
    public ResponseEntity<?> newGame(@RequestBody Map<String,String> flashcardSetInfo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        int flashcardSetId = Integer.parseInt(flashcardSetInfo.get("set_id"));
        FlashcardSet flashcardSet = flashcardSetService.findById(flashcardSetId);
        if(flashcardSet==null){
            return new ResponseEntity<>("\"there's no set with that id\"",HttpStatus.NOT_FOUND);
        }
        if(!username.equals(flashcardSet.getUsername())) {
            return new ResponseEntity<>("\"you don't have permission to do that\"", HttpStatus.FORBIDDEN);
        }
        return quizService.newGame(flashcardSet,userRepository.findByEmail(username));
    }

    @PostMapping("/guess")
    @ApiOperation(value = "attempt at guessing in quiz game")
    public ResponseEntity<?> guess(@RequestBody Map<String,String> guessInfo){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByEmail(username);
        int gameId = Integer.parseInt(guessInfo.get("game_id"));
        String guess = guessInfo.get("guess");
        QuizGame game = quizService.findById(gameId);
        List<QuizGame> userGames = quizService.getGamesByUser(user);
        if(game==null){
            return new ResponseEntity<>("\"there's no game with that id\"",HttpStatus.NOT_FOUND);
        }
        if(!userGames.contains(game)){
            return new ResponseEntity<>("\"you don't have permission to do that\"",HttpStatus.FORBIDDEN);
        }
        return quizService.makeGuess(game,guess);
    }

    @GetMapping("/games")
    @ApiOperation(value = "get all quiz games for user")
    public ResponseEntity<?> getAllUserGames(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return new ResponseEntity<>(quizService.getGamesByUser(userRepository.findByEmail(username)),HttpStatus.OK);
    }
}
