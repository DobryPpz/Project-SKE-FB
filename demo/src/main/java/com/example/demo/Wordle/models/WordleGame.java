package com.example.demo.Wordle.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.*;

@JsonPropertyOrder({"gameID", "status", "word", "guessesLeft", "guess", "hint", "notUsedLetters"})
public class WordleGame {
    private final int gameId;
    private final String word;
    private String guess;
    private WordleGameStatus status;
    private Map<Character, LetterState> letterStates;
    private int guessCount;
    private final int maxGuessCount = 6;

    public WordleGame(List<String> listOfWords, int gameId) {
        this.gameId = gameId;
        Random random = new Random();
        this.word = getRandomWord(listOfWords);
        this.guess = "";
        this.status = WordleGameStatus.ACTIVE;
        this.letterStates = initializeLetterStates();
        this.guessCount = 0;
    }

    public int getGameID() {
        return gameId;
    }

    public String getWord() {
        return word;
    }

    public String getGuess() {
        return guess;
    }

    public WordleGameStatus getStatus() {
        return status;
    }

    public Set<Character> getNotUsedLetters() {
        Set<Character> notUsedLetters = new HashSet<>();

        for (Map.Entry<Character, LetterState> entry : letterStates.entrySet()) {
            if (entry.getValue() == LetterState.NOT_USED) {
                notUsedLetters.add(entry.getKey());
            }
        }

        return notUsedLetters;
    }

    @JsonIgnore
    public int getGuessCount() {
        return guessCount;
    }

    @JsonIgnore
    public int getMaxGuessCount() {
        return maxGuessCount;
    }

    public int getGuessesLeft() {
        return maxGuessCount - guessCount;
    }

    public List<String> getHint() {
        List<String> hint = new ArrayList<>();
        for (int i = 0; i < guess.length(); i++) {
            char c = guess.charAt(i);
            if (letterStates.containsKey(c)) {
                if (word.charAt(i) == c) {
                    hint.add(c + " - GREEN");
                } else if (word.contains(Character.toString(c))) {
                    hint.add(c + " - YELLOW");
                } else {
                    hint.add(c + " - GREY");
                }
            }
        }
        return hint;
    }

    public void makeGuess(String guess) {
        if (status != WordleGameStatus.ACTIVE) {
            return;
        }

        this.guessCount++;
        this.guess = guess;

        for (int i = 0; i < guess.length(); i++) {
            char c = guess.charAt(i);
            if (letterStates.containsKey(c) && letterStates.get(c) != LetterState.NOT_USED) {
                continue;
            }

            if (word.contains(Character.toString(c))) {
                if (guess.charAt(i) == word.charAt(i)) {
                    letterStates.put(c, LetterState.GREEN);
                } else {
                    letterStates.put(c, LetterState.YELLOW);
                }
            } else {
                letterStates.put(c, LetterState.GREY);
            }
        }

        if (guess.equals(word)) {
            status = WordleGameStatus.WON;
        } else if (guessCount + 1 > maxGuessCount) {
            status = WordleGameStatus.LOST;
        }
    }

    private static String getRandomWord(List<String> listOfWords) {
        Random random = new Random();
        int index = random.nextInt(listOfWords.size());
        return listOfWords.get(index);
    }

    private Map<Character, LetterState> initializeLetterStates() {
        Map<Character, LetterState> letterStates = new HashMap<>();

        for (char c = 'a'; c <= 'z'; c++) {
            letterStates.put(c, LetterState.NOT_USED);
        }

        return letterStates;
    }
}