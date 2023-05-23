package com.example.demo.Hangman.models;
import java.util.List;
import java.util.Random;

import com.example.demo.CustomUserDetails;
import com.example.demo.Fiszki.models.FlashcardSet;
import com.example.demo.Hangman.other.TempClassForWords;
import com.example.demo.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Entity
@Table(name = "hangman_games")
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

    @ManyToOne(cascade =
            {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name="user_id")
    private User user;

    /*public HangmanGame() {
    }*/

    public HangmanGame(FlashcardSet flashcardSet,String side, User user){
        this.word = getRandomWord(flashcardSet,side);
        this.guessesLeft = MAX_NR_OF_TRIES;
        this.guessedWord = getEmptyWord(this.word.length());
        this.setStatus();
        this.user = user;
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

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    private static String getRandomWord(FlashcardSet flashcardSet,String side){
        var flashcards = flashcardSet.getFlashcards();
        Random random = new Random();
        int index = random.nextInt(flashcards.size());
        if (side.equals("front")) return flashcards.get(index).getFront();
        else return flashcards.get(index).getBack();

    }

    private static String getEmptyWord(int word_len){
        StringBuilder sb = new StringBuilder();
        for(int i=0; i < word_len; i++) {
            sb.append("_");
        }
        return sb.toString();
    }

}
