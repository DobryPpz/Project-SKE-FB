package com.example.demo.Hangman.exceptions;


public class GameDoesNotExistException extends Exception{
    public GameDoesNotExistException(String id){
       super(String.format("Game with id: "+id+" does not exist."));
    }
}

