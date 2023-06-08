//package com.example.demo.Wordle.models;
//
//import com.example.demo.Fiszki.models.FlashcardSet;
//import com.example.demo.Login.models.User;
//import com.example.demo.Wordle.models.WordleGame;
//import com.example.demo.Wordle.other.WordleWords;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//
//import java.util.*;
//
////@JsonPropertyOrder({"gameID", "status", "word", "guessesLeft", "guess", "hint", "notUsedLetters"})
//@Entity
//@Table(name = "wordle_games")
//public class WordleGame {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "game_id")
//    private int gameId;
//
//    @Column(name = "word",nullable = false)
//    private String word;
//
//    @Column(name = "guess",nullable = false)
//    private String guess;
//
//    @Enumerated(EnumType.STRING)
//    private WordleGameStatus status;
//
//    @Column(name = "guessCount",nullable = false)
//    private int guessCount;
//
//    private static final int maxGuessCount = 6;
//
//    @ManyToOne(cascade =
//            {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
//    @JoinColumn(name="user_id")
//    private User user;
//
//    public WordleGame(FlashcardSet flashcardSet, String side, User user){
//        this.word = getRandomWord(flashcardSet, side);
//        this.guess = getEmptyWord(this.word.length());
//        this.setStatus();
//        this.user = user;
//    }
//
//    public int getGameID() {
//        return gameId;
//    }
//
//    public String getWord() {
//        return word;
//    }
//
//    public String getGuess() {
//        return guess;
//    }
//
//    public WordleGameStatus getStatus() {
//        return status;
//    }
//
//    public void setStatus(){
//        if(word.equals(guess)) {
//            this.status = WordleGameStatus.WON;
//        }
//        else{
//            if(guessCount > 0) {
//                this.status = WordleGameStatus.ACTIVE;
//            }
//            if(guessCount <= 0){
//                this.status = WordleGameStatus.LOST;
//            }
//        }
//    }
//
//    public Set<Character> getNotUsedLetters() {
//        Set<Character> notUsedLetters = new HashSet<>();
//
//        for (char c = 'a'; c <= 'z'; c++) {
//            if (!guess.contains(Character.toString(c))) {
//                notUsedLetters.add(c);
//            }
//        }
//
//        return notUsedLetters;
//    }
//
//    @JsonIgnore
//    public int getGuessCount() {
//        return guessCount;
//    }
//
//    @JsonIgnore
//    public int getMaxGuessCount() {
//        return maxGuessCount;
//    }
//
//    public int getGuessesLeft() {
//        return maxGuessCount - guessCount;
//    }
//
//    public List<String> getHint() {
//        List<String> hint = new ArrayList<>();
//
//        for (int i = 0; i < guess.length(); i++) {
//            char c = guess.charAt(i);
//
//            if (word.charAt(i) == c) {
//                hint.add(c + " - GREEN");
//            } else if (word.contains(Character.toString(c))) {
//                hint.add(c + " - YELLOW");
//            } else {
//                hint.add(c + " - GREY");
//            }
//        }
//
//        return hint;
//    }
//
//    public void makeGuess(String guess) {
//        if (status != WordleGameStatus.ACTIVE) {
//            return;
//        }
//
//        this.guessCount++;
//        this.guess = guess;
//
//        if (guess.equals(word)) {
//            status = WordleGameStatus.WON;
//        } else if (guessCount >= maxGuessCount) {
//            status = WordleGameStatus.LOST;
//        }
//    }
//
//    private static String getRandomWord(FlashcardSet flashcardSet,String side){
//        var flashcards = flashcardSet.getFlashcards();
//        Random random = new Random();
//        int index = random.nextInt(flashcards.size());
//        if (side.equals("front")) return flashcards.get(index).getFront();
//        else return flashcards.get(index).getBack();
//    }
//
//    private static String getEmptyWord(int word_len){
//        StringBuilder sb = new StringBuilder();
//        for(int i=0; i < word_len; i++) {
//            sb.append("_");
//        }
//        return sb.toString();
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
//
//    public User getUser() {
//        return user;
//    }
//}

package com.example.demo.Wordle.models;
import java.util.List;
import java.util.Random;


import com.example.demo.Fiszki.models.FlashcardSet;
import com.example.demo.Wordle.other.WordleWords;

import com.example.demo.Login.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Entity
@Table(name = "wordle_games")
public class WordleGame {

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
    private WordleGameStatus status;

    @Column(name = "guesses_left",nullable = false)
    private int guessesLeft;
    private static final int MAX_NR_OF_TRIES = 7;

    @ManyToOne(cascade =
            {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name="user_id")
    private User user;

    public WordleGame() {
    }

    public WordleGame(FlashcardSet flashcardSet,String side, User user){
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

    public WordleGameStatus getStatus(){
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
            this.status = WordleGameStatus.WON;
        }
        else{
            if(guessesLeft > 0) {
                this.status = WordleGameStatus.ACTIVE;
            }
            if(guessesLeft <= 0){
                this.status = WordleGameStatus.LOST;
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
