package com.example.demo.Fiszki.controllers;
import com.example.demo.Fiszki.models.Flashcard;
import com.example.demo.Fiszki.models.FlashcardSet;
import com.example.demo.Fiszki.service.FlashcardService;
import com.example.demo.Fiszki.service.FlashcardSetService;
import com.example.demo.Fiszki.service.FlashcardSetServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/set/flashcard")
    public FlashcardSet addFlashcardToSet(@RequestBody Map<String,String> flashcard){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        int flashcardSetId = Integer.parseInt(flashcard.get("set_id"));
        String front = flashcard.get("front");
        String back = flashcard.get("back");
        FlashcardSet flashcardSet = flashcardSetService.findById(flashcardSetId);
        if(flashcardSet==null || !username.equals(flashcardSet.getUsername())){
            return null;
        }
        Flashcard newFlashcard = new Flashcard(front,back,flashcardSet);
        flashcardSet.addFlashcard(newFlashcard);
        newFlashcard = flashcardService.save(newFlashcard);
        return flashcardSet;
    }
}
