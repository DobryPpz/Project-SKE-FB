package com.example.demo.Hangman.models;
import java.util.List;
import java.util.Random;

import com.example.demo.Hangman.other.TempClassForWords;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "hangman_game")
public class HangmanGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id")
    private int gameId;
    //jeszcze kategoria slowa się przyda
    @Column(name = "word",nullable = false)
    private String word;
    @Column(name = "guessed_word",nullable = false)
    private String guessedWord;
    @Enumerated(EnumType.STRING)
    private HangmanGameStatus status;
    @Column(name = "guesses_left",nullable = false)
    private int guessesLeft;
    private static final int MAX_NR_OF_TRIES = 7;

    /*public HangmanGame() {
    }*/

    public HangmanGame(List<String> listOfAllWords, int gameId){
        this.gameId = gameId;
        this.word = getRandomWord(listOfAllWords);
        this.guessesLeft = MAX_NR_OF_TRIES;
        this.guessedWord = getEmptyWord(this.word.length());
        this.setStatus();
    }
    public HangmanGame(List<String> listOfAllWords){
        this.word = getRandomWord(listOfAllWords);
        this.guessesLeft = MAX_NR_OF_TRIES;
        this.guessedWord = getEmptyWord(this.word.length());
        this.setStatus();
    }

    public HangmanGame(){
        this.word = getRandomWord(TempClassForWords.getWords());
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
    public void incIncorrectGuesses(){
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
