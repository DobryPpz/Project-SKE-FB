package com.example.demo.Hangman.models;
import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class HangmanGame {

    private final int gameId;
    //jeszcze kategoria slowa się przyda
    private final String word;
    private String guessedWord;
    private HangmanGameStatus status;

    private int guessesLeft;
    private static final int MAX_NR_OF_TRIES = 7;

    public HangmanGame(List<String> listOfAllWords, int gameId){
        this.gameId = gameId;
        this.word = getRandomWord(listOfAllWords);
        this.guessesLeft = MAX_NR_OF_TRIES;
        this.guessedWord = getEmptyWord(this.word.length());
        this.setStatus();
    }

    public int getId(){
        return gameId;
    }

    @JsonIgnore
    public String getWord(){
        return word;
    }

    public String getGuessedWord(){
        return guessedWord;
    }

    public int getGuessesLeft(){
        return guessesLeft;
    }

    public HangmanGameStatus getStatus(){
        return status;
    }

    public void setStatus(){
        if(word.equals(guessedWord)) {
            this.status = HangmanGameStatus.WON;
        }
        else{
            if(guessesLeft > 0) {
                this.status = HangmanGameStatus.ACTIVE;
            }
            if(guessesLeft <= 0){
                this.status = HangmanGameStatus.LOST;
            }
        }
    }
    public void incIncorrect_guesses(){
        this.guessesLeft--;
        this.setStatus();
    }

    public void setGuessedWord(Character c) {

        StringBuilder tempGuessedWord = new StringBuilder(this.guessedWord);
        for(int i=0; i < this.word.length(); i++) {
            Character wc = this.word.charAt(i);
            if (wc.equals(c)) {
                tempGuessedWord.setCharAt(i,c);
            }
        }
        this.guessedWord = tempGuessedWord.toString();
        this.setStatus();
    }

    private static String getRandomWord(List<String> listOfAllWords){
        Random random = new Random();
        int index = random.nextInt(listOfAllWords.size());
        return listOfAllWords.get(index);
    }

    private static String getEmptyWord(int word_len){
        StringBuilder sb = new StringBuilder();
        for(int i=0; i < word_len; i++) {
            sb.append("_");
        }
        return sb.toString();
    }

}
