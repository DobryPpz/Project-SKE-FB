package com.example.demo.Hangman.controllers;

import com.example.demo.Hangman.models.HangmanGame;
import com.example.demo.Hangman.service.HangmanService;
import com.example.demo.ModzzakfransiemApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration(classes = ModzzakfransiemApplication.class)
@AutoConfigureMockMvc
class HangmanControllerTest {
    @Autowired
    private MockMvc mockmvc;

    @MockBean
    private HangmanService hangmanService;

//    @Test
//    public void get_games_from_hangmanservice() throws Exception {
//        HangmanGame hangmanGame = new HangmanGame();
//
//        when(hangmanService.getAllCurrentGames()).thenReturn(List.of(hangmanGame));
//
//        mockmvc.perform(get("/hangman/current_games").contentType("application/json"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.[0].guessedWord").exists())
//                .andExpect(jsonPath("$.[0].status").value("ACTIVE"))
//                .andExpect(jsonPath("$.[0].guessesLeft").value(7))
//                .andExpect(jsonPath("$.[0].id").exists())
//                .andExpect(jsonPath("$.[1].id").doesNotExist());
//
//        verify(hangmanService,times(1)).getAllCurrentGames();
//    }

}