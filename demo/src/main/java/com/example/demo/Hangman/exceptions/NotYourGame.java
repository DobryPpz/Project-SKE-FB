package com.example.demo.Hangman.exceptions;

public class NotYourGame extends Exception{

    public NotYourGame() {
        super(String.format("This is not your game!"));
    }
}
