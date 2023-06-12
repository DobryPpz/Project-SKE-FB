package com.example.demo.Fiszki.controllers;
import com.example.demo.Fiszki.dto.*;
import com.example.demo.Fiszki.models.Flashcard;
import com.example.demo.Fiszki.models.FlashcardSet;
import com.example.demo.Fiszki.service.FlashcardService;
import com.example.demo.Fiszki.service.FlashcardSetService;
import com.example.demo.Fiszki.service.FlashcardSetServiceImpl;
import com.example.demo.Login.services.UserService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user/flashcards")
@Tag(name = "flashcard controller")
public class FlashcardController {
    private FlashcardService flashcardService;
    private FlashcardSetService flashcardSetService;
    private UserService userService;
    @Autowired
    public FlashcardController(FlashcardService flashcardService, FlashcardSetService flashcardSetService, UserService userService){
        this.flashcardService = flashcardService;
        this.flashcardSetService = flashcardSetService;
        this.userService = userService;
    }

    @GetMapping("/set")
    @Operation(summary = "get all the user's sets")
    @ApiResponses(value = {
            @ApiResponse(code = 200,response = ArrayList.class, message = "success")
    })
    public List<FlashcardSet> allSets(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return flashcardSetService.findAllByUser(username);
    }

    @PostMapping("/set")
    @Operation(summary = "add set")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = MessageResponse.class, message = "flashcard set added")
    })
    public ResponseEntity<?> addFlashcardSet(@RequestBody PostFlashcardSet flashcardSetMap){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String name = flashcardSetMap.getName();
        FlashcardSet flashcardSet = new FlashcardSet(name,username);
        FlashcardSet dbSet = flashcardSetService.save(flashcardSet);
        return new ResponseEntity<>(new MessageResponse("flashcard set added"),HttpStatus.OK);
    }

    @DeleteMapping("/set")
    @Operation(summary = "delete set")
    @ApiResponses(value = {
            @ApiResponse(code = 404,response = MessageResponse.class,message = "the set with that id does not exist"),
            @ApiResponse(code = 403,response = MessageResponse.class,message = "you don't have permission to do that"),
            @ApiResponse(code = 200,response = MessageResponse.class,message = "set successfully deleted")
    })
    public ResponseEntity<?> deleteFlashcardSet(@RequestBody DeleteFlashcardSet flashcardSetMap){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        int flashcardSetId = flashcardSetMap.getSet_id();
        FlashcardSet flashcardSet = flashcardSetService.findById(flashcardSetId);
        if(flashcardSet==null){
            return new ResponseEntity<>(new MessageResponse("the set with that id does not exist"),HttpStatus.NOT_FOUND);
        }
        if(!username.equals(flashcardSet.getUsername())){
            return new ResponseEntity<>(new MessageResponse("you don't have permission to do that"),HttpStatus.FORBIDDEN);
        }
        flashcardSetService.deleteById(flashcardSetId);
        return new ResponseEntity<>(new MessageResponse("set successfully deleted"),HttpStatus.OK);
    }

    @PostMapping("/set/flashcard")
    @Operation(summary = "add flashcard to set")
    @ApiResponses(value = {
            @ApiResponse(code = 404,response = MessageResponse.class,message = "the set with that id does not exist"),
            @ApiResponse(code = 403,response = MessageResponse.class,message = "you don't have permission to do that"),
            @ApiResponse(code = 200,response = MessageResponse.class,message = "flashcard successfully added")
    })
    public ResponseEntity<?> addFlashcardToSet(@RequestBody PostFlashcard flashcardMap ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        int flashcardSetId = flashcardMap.getSet_id();
        String front = flashcardMap.getFront();
        String back = flashcardMap.getBack();
        FlashcardSet flashcardSet = flashcardSetService.findById(flashcardSetId);
        if(flashcardSet==null){
            return new ResponseEntity<>(new MessageResponse("the set with that id does not exist"),HttpStatus.NOT_FOUND);
        }
        if(!username.equals(flashcardSet.getUsername())){
            return new ResponseEntity<>(new MessageResponse("you don't have permission to do that"),HttpStatus.FORBIDDEN);
        }
        Flashcard newFlashcard = new Flashcard(front,back,flashcardSet);
        flashcardSet.addFlashcard(newFlashcard);
        newFlashcard = flashcardService.save(newFlashcard);
        return new ResponseEntity<>(new MessageResponse("flashcard successfully added"),HttpStatus.OK);
    }

    @PutMapping("/set/flashcard")
    @Operation(summary = "change flashcard")
    @ApiResponses(value = {
            @ApiResponse(code = 404,response = MessageResponse.class,message = "the flashcard with that id does not exist"),
            @ApiResponse(code = 403,response = MessageResponse.class,message = "you don't have permission to do that"),
            @ApiResponse(code = 200,response = MessageResponse.class,message = "flashcard changed")
    })
    public ResponseEntity<?> changeFlashcard(@RequestBody PutFlashcard flashcardMap){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        int flashcardId = flashcardMap.getId();
        Flashcard flashcard = flashcardService.findById(flashcardId);
        if(flashcard==null){
            return new ResponseEntity<>(new MessageResponse("the flashcard with that id does not exist"),HttpStatus.NOT_FOUND);
        }
        FlashcardSet flashcardSet = flashcard.getFlashcardSet();
        if(!username.equals(flashcardSet.getUsername())){
            return new ResponseEntity<>(new MessageResponse("you don't have permission to do that"),HttpStatus.FORBIDDEN);
        }
        String changedFront = flashcardMap.getFront();
        String changedBack = flashcardMap.getBack();
        if(changedFront!=null){
            flashcard.setFront(changedFront);
        }
        if(changedBack!=null){
            flashcard.setBack(changedBack);
        }
        Flashcard changedFlashcard = flashcardService.save(flashcard);
        return new ResponseEntity<>(new MessageResponse("flashcard changed"),HttpStatus.OK);
    }

    @DeleteMapping("/set/flashcard")
    @Operation(summary = "change flashcard")
    @ApiResponses(value = {
            @ApiResponse(code = 404,response = MessageResponse.class,message = "the flashcard with that id does not exist"),
            @ApiResponse(code = 403,response = MessageResponse.class,message = "you don't have permission to do that"),
            @ApiResponse(code = 200,response = MessageResponse.class,message = "flashcard successfully deleted")
    })
    public ResponseEntity<?> deleteFlashcard(@RequestBody DeleteFlashcard flashcardMap){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        int flashcardId = flashcardMap.getId();
        Flashcard flashcard = flashcardService.findById(flashcardId);
        if(flashcard==null){
            return new ResponseEntity<>(new MessageResponse("the flashcard with that id does not exist"),HttpStatus.NOT_FOUND);
        }
        FlashcardSet flashcardSet = flashcard.getFlashcardSet();
        if(!username.equals(flashcardSet.getUsername())){
            return new ResponseEntity<>(new MessageResponse("you don't have permission to do that"),HttpStatus.FORBIDDEN);
        }
        flashcardService.deleteById(flashcardId);
        return new ResponseEntity<>(new MessageResponse("flashcard successfully deleted"),HttpStatus.OK);
    }
}

