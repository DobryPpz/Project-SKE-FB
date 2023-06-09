package com.example.demo.Login.controllers;


import com.example.demo.Fiszki.models.FlashcardSet;
import com.example.demo.Fiszki.service.FlashcardSetService;
import com.example.demo.Login.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    UserService userService;

    FlashcardSetService flashcardSetService;

    //@GetMapping("/{id}")
    @GetMapping("")
    public String registrationForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        long id = userService.findUserByEmail(authentication.getName()).getId();
        model.addAttribute("flashcards", flashcardSetService.findAllByUser(authentication.getName()));
        return "user";
    }

    @GetMapping("/flashcards/set/flashcard")
    public String addFlashcardToSet(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("flashcards", flashcardSetService.findAllByUser(authentication.getName()));
        return "addflashcard";
    }

    @GetMapping("/menu")
    public String seeMenu() {
        return "gamesmenu";
    }

    @GetMapping("/hangman")
    public String hangmanGame() {
        return "hangman";
    }

    @GetMapping("/wordle")
    public String wordleGame() {
        return "wordle";
    }

    @GetMapping("/flashcards/add_flashcard_set")
    public String addFlashcardSet() {
        return "addflashcardset";
    }

    @GetMapping("/flashcards/delete_flashcard_set")
    public String deleteFlashcardSet(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("flashcards", flashcardSetService.findAllByUser(authentication.getName()));
        return "deleteflashcardset";
    }

    @GetMapping("/flashcards/delete_flashcard")
    public String deleteFlashcard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("flashcards", flashcardSetService.findAllByUser(authentication.getName()));
        return "deleteflashcard";

    }

    @GetMapping("/flashcards/update_flashcard")
    public String updateFlashcard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("flashcards", flashcardSetService.findAllByUser(authentication.getName()));
        return "updateflashcard";
    }
}
