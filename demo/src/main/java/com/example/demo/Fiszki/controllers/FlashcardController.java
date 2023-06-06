package com.example.demo.Fiszki.controllers;
import com.example.demo.Fiszki.models.Flashcard;
import com.example.demo.Fiszki.models.FlashcardSet;
import com.example.demo.Fiszki.service.FlashcardService;
import com.example.demo.Fiszki.service.FlashcardSetService;
import com.example.demo.Fiszki.service.FlashcardSetServiceImpl;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user/flashcards")
public class FlashcardController {
    private FlashcardService flashcardService;
    private FlashcardSetService flashcardSetService;
    @Autowired
    public FlashcardController(FlashcardService flashcardService, FlashcardSetService flashcardSetService){
        this.flashcardService = flashcardService;
        this.flashcardSetService = flashcardSetService;
    }

    @GetMapping("/set")
    public List<FlashcardSet> allSets(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return flashcardSetService.findAllByUser(username);
    }

    @PostMapping("/set")
    public FlashcardSet addFlashcardSet(@RequestBody Map<String,String> flashcardSetMap){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String name = flashcardSetMap.get("name");
        FlashcardSet flashcardSet = new FlashcardSet(name,username);
        FlashcardSet dbSet = flashcardSetService.save(flashcardSet);
        return dbSet;
    }

    @DeleteMapping("/set")
    public ResponseEntity<?> deleteFlashcardSet(@RequestBody Map<String,String> flashcardSetMap){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        int flashcardSetId = Integer.parseInt(flashcardSetMap.get("set_id"));
        FlashcardSet flashcardSet = flashcardSetService.findById(flashcardSetId);
        if(flashcardSet==null){
            return new ResponseEntity<>("the set with that id does not exist",HttpStatus.NOT_FOUND);
        }
        if(!username.equals(flashcardSet.getUsername())){
            return new ResponseEntity<>("you don't have permission to do that",HttpStatus.FORBIDDEN);
        }
        flashcardSetService.deleteById(flashcardSetId);
        return new ResponseEntity<>("set successfully deleted",HttpStatus.NO_CONTENT);
    }

    @PostMapping("/set/flashcard")
    public ResponseEntity<?> addFlashcardToSet(@RequestBody Map<String,String> flashcardMap){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        int flashcardSetId = Integer.parseInt(flashcardMap.get("set_id"));
        String front = flashcardMap.get("front");
        String back = flashcardMap.get("back");
        FlashcardSet flashcardSet = flashcardSetService.findById(flashcardSetId);
        if(flashcardSet==null){
            return new ResponseEntity<>("the set with that id does not exist",HttpStatus.NOT_FOUND);
        }
        if(!username.equals(flashcardSet.getUsername())){
            return new ResponseEntity<>("you don't have permission to do that",HttpStatus.FORBIDDEN);
        }
        Flashcard newFlashcard = new Flashcard(front,back,flashcardSet);
        flashcardSet.addFlashcard(newFlashcard);
        newFlashcard = flashcardService.save(newFlashcard);
        return new ResponseEntity<>("flashcard successfully added",HttpStatus.OK);
    }

    @PutMapping("/set/flashcard")
    public ResponseEntity<?> changeFlashcard(@RequestBody Map<String,String> flashcardMap){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        int flashcardId = Integer.parseInt(flashcardMap.get("id"));
        Flashcard flashcard = flashcardService.findById(flashcardId);
        if(flashcard==null){
            return new ResponseEntity<>("the flashcard with that id does not exist",HttpStatus.NOT_FOUND);
        }
        FlashcardSet flashcardSet = flashcard.getFlashcardSet();
        if(!username.equals(flashcardSet.getUsername())){
            return new ResponseEntity<>("you don't have permission to do that",HttpStatus.FORBIDDEN);
        }
        String changedFront = flashcardMap.get("front");
        String changedBack = flashcardMap.get("back");
        if(changedFront!=null){
            flashcard.setFront(changedFront);
        }
        if(changedBack!=null){
            flashcard.setBack(changedBack);
        }
        Flashcard changedFlashcard = flashcardService.save(flashcard);
        return new ResponseEntity<>("flashcard changed",HttpStatus.OK);
    }

    @DeleteMapping("/set/flashcard")
    public ResponseEntity<?> deleteFlashcard(@RequestBody Map<String,String> flashcardMap){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        int flashcardId = Integer.parseInt(flashcardMap.get("id"));
        Flashcard flashcard = flashcardService.findById(flashcardId);
        if(flashcard==null){
            return new ResponseEntity<>("the flashcard with that id does not exist",HttpStatus.NOT_FOUND);
        }
        FlashcardSet flashcardSet = flashcard.getFlashcardSet();
        if(!username.equals(flashcardSet.getUsername())){
            return new ResponseEntity<>("you don't have permission to do that",HttpStatus.FORBIDDEN);
        }
        flashcardService.deleteById(flashcardId);
        return new ResponseEntity<>("flashcard successfully deleted",HttpStatus.NO_CONTENT);
    }
}
