package com.example.demo.Quiz.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QuizGameDTO {
    @JsonProperty("message")
    private String message;
    @JsonProperty("pointsWon")
    private Integer pointsWon;
    @JsonProperty("toGuess")
    private String toGuess;
    @JsonProperty("game_id")
    private int game_id;
    public QuizGameDTO(String message, Integer pointsWon, String toGuess, int game_id){
        this.message = message;
        this.pointsWon = pointsWon;
        this.toGuess = toGuess;
        this.game_id = game_id;
    }
}
