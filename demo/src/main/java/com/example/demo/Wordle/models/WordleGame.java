package com.example.demo.Wordle.models;

import com.example.demo.Fiszki.models.Flashcard;
import com.example.demo.Fiszki.models.FlashcardSet;
import com.example.demo.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import jakarta.persistence.Table;


import java.util.*;

@Entity
@Table(name = "wordle_games")
@JsonPropertyOrder({"gameID", "status", "word", "guessesLeft", "guess", "hint", "notUsedLetters"})
public class WordleGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id")
    private int gameId;

    @Column(name = "word", nullable = false)
    private String word;

    @Column(name = "guess")
    private String guess;

    @Enumerated(EnumType.STRING)
    private WordleGameStatus status;

    @ElementCollection
    @CollectionTable(name = "letter_states",
            joinColumns = @JoinColumn(name = "game_id"))
    @MapKeyColumn(name = "letter")
    @Column(name = "state")
    private Map<Character, LetterState> letterStates;

    @Column(name = "guess_count")
    private int guessCount;

    @Column(name = "max_guess_count")
    private final int maxGuessCount = 6;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;

    public WordleGame(FlashcardSet flashcardSet, String side, User user) {
        this.word = getRandomWord(flashcardSet, side);
        this.guess = "";
        this.status = WordleGameStatus.ACTIVE;
        this.letterStates = initializeLetterStates();
        this.guessCount = 0;
        this.user = user;
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

    private static String getRandomWord(FlashcardSet flashcardSet, String side) {
        List<Flashcard> flashcards = flashcardSet.getFlashcards();
        Random random = new Random();
        int index = random.nextInt(flashcards.size());
        if (side.equals("front")) {
            return flashcards.get(index).getFront().toLowerCase();
        } else {
            return flashcards.get(index).getBack().toLowerCase();
        }
    }


    private Map<Character, LetterState> initializeLetterStates() {
        Map<Character, LetterState> letterStates = new HashMap<>();

        for (char c = 'a'; c <= 'z'; c++) {
            letterStates.put(c, LetterState.NOT_USED);
        }

        return letterStates;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setGuessedWord(char guess0) {
    }

    public void incIncorrectGuesses() {

    }
}