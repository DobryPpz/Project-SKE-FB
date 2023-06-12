package com.example.demo.Quiz.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QuizGameDTO {
    @JsonProperty("message")
    private String message;
    @JsonProperty("pointsWon")
    private Integer pointsWon;
    @JsonProperty("toGuess")
    private String toGuess;
    public QuizGameDTO(String message, Integer pointsWon, String toGuess){
        this.message = message;
        this.pointsWon = pointsWon;
        this.toGuess = toGuess;
    }
}
