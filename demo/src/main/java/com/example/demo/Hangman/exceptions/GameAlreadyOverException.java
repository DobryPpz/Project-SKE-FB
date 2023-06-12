package com.example.demo.Hangman.exceptions;


public class GameAlreadyOverException extends Exception
{
    public GameAlreadyOverException() {
        super(String.format("Game already ended"));
    }
}