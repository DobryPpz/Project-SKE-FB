package com.example.demo.Login.controllers;


import com.example.demo.Fiszki.models.FlashcardSet;
import com.example.demo.Fiszki.service.FlashcardSetService;
import com.example.demo.Login.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    @GetMapping("/")
    public String registrationForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        long id = userService.findUserByEmail(authentication.getName()).getId();
        model.addAttribute("flashcards", flashcardSetService.findAllByUser(userService.findUserByEmail(authentication.getName()).getUsername()));
        return "user";
    }

    @GetMapping("/flashcards/set/flashcard")
    public String addFlashcardToSet(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("flashcards", flashcardSetService.findAllByUser(userService.findUserByEmail(authentication.getName()).getUsername()));
        return "addflashcard";
    }

}
