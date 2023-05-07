package com.example.demo.Wordle.exceptions;


public class InvalidGuessException extends Exception
{
    public InvalidGuessException() {
        super(String.format("Game is already complete"));
    }
}