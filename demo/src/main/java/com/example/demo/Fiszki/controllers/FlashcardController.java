package com.example.demo.Fiszki.controllers;
import com.example.demo.Fiszki.models.FlashcardSet;
import com.example.demo.Fiszki.service.FlashcardService;
import com.example.demo.Fiszki.service.FlashcardSetService;
import com.example.demo.Fiszki.service.FlashcardSetServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/set")
    public FlashcardSet addFlashcardSet(@RequestBody FlashcardSet flashcardSet){
        flashcardSet.setId(0);
        FlashcardSet dbSet = flashcardSetService.save(flashcardSet);
        return dbSet;
    }
}
