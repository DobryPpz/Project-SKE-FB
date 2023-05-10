package com.example.demo.Wordle.exceptions;

public class InvalidGuessException extends Exception
{
    public InvalidGuessException(String guess) {
        super(String.format("Invalid guess: %s is incorrect", guess));
    }
}
//s