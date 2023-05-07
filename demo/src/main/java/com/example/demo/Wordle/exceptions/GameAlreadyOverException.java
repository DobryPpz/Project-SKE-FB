package com.example.demo.Wordle.exceptions;


public class GameAlreadyOverException extends Exception
{
    public GameAlreadyOverException() {
        super(String.format("Game already ended"));
    }
}