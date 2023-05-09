package com.example.demo.Wordle.models;
import java.util.List;
import java.util.Scanner;
import java.util.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class WordleGame {
    private final int gameId;
    private final String word;

    private String guessedWord;
    private String guess;
    private int guessCount;
    private final int maxGuessCount = 6;
    private WordleGameStatus status;

    public WordleGame(List<String> listOfWords, int gameId) {
        this.gameId = gameId;
        Random random = new Random();
        this.word = getRandomWord(listOfWords);
        this.guess = "";
        this.guessCount = 0;
        this.status = WordleGameStatus.ACTIVE;
    }

    @JsonIgnore
    public String getWord(){
        return word;
    }
    public int getGameID() {
        return gameId;
    }

    public String getGuess() {
        return guess;
    }

    public String getGuessedWord(){
        return guessedWord;
    }

    public int getGuessCount() {
        return guessCount;
    }

    public WordleGameStatus getStatus() {
        return status;
    }

    public Set<Character> getUnsolvedLetters() {
        Set<Character> unsolvedLetters = new HashSet<>();
        Set<Character> guessedLetters = new HashSet<>();

        //dodaje wszystkie litery do unsolvedLetters
        for (char c : guess.toCharArray()) {
            unsolvedLetters.add(c);
        }

        //dodaje odgadniete do guessed
        for (char c : guess.toString().toCharArray()) {
            if (Character.isLetter(c)) {
                guessedLetters.add(Character.toLowerCase(c));
            }
        }

        //usuwa litery ktorych nie ma
        unsolvedLetters.removeAll(guessedLetters);

        return unsolvedLetters;
    }

    public void makeGuess(String guess) {
        if (status != WordleGameStatus.ACTIVE) {
            return;
        }

        this.guessCount++;
        this.guess = guess;

        Set<Character> correctChars = new HashSet<>();
        Set<Character> incorrectChars = new HashSet<>();

        for (int i = 0; i < guess.length(); i++) {
            char c = guess.charAt(i);
            if (guess.charAt(i) == c) {
                correctChars.add(c);
            } else if (guess.contains(Character.toString(c))) {
                incorrectChars.add(c);
            }
        }

        if (correctChars.size() == guess.length()) {
            status = WordleGameStatus.WON;
        } else if (guessCount >= maxGuessCount) {
            status = WordleGameStatus.LOST;
        }
    }

    public void setGuessedWord(Character c) {
        char[] tempGuessedWord = this.word.toCharArray();
        for(int i=0; i < this.word.length(); i++) {
            Character wc = this.word.charAt(i);
            if (wc.equals(c)) {
                tempGuessedWord[i] = c;
            }
        }
        this.guessedWord = new String(tempGuessedWord);
        this.setStatus();
    }

    private static String getRandomWord(List<String> listOfAllWords){
        Random random = new Random();
        int index = random.nextInt(listOfAllWords.size());
        return listOfAllWords.get(index);
    }

    private static String getEmptyWord(int word_len){
        char[] emptyWord = new char[word_len];
        Arrays.fill(emptyWord, '_');
        return new String(emptyWord);
    }

    public void setStatus(){
        if(word.equals(guessedWord)) {
            this.status = WordleGameStatus.WON;
        }
        else{
            if(guessCount > 0) {
                this.status = WordleGameStatus.ACTIVE;
            }
            if(guessCount >= maxGuessCount){
                this.status = WordleGameStatus.LOST;
            }
        }
    }
    public void incIncorrect_guesses(){
        this.guessCount++;
        this.setStatus();
    }
}
