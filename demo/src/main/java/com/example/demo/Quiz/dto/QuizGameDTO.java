package com.example.demo.Quiz.dto;

public class QuizGameDTO {
    private String message;
    private Integer pointsWon;
    private String toGuess;
    public QuizGameDTO(String message, Integer pointsWon, String toGuess){
        this.message = message;
        this.pointsWon = pointsWon;
        this.toGuess = toGuess;
    }
}
