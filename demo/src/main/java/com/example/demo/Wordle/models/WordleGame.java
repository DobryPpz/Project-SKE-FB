package com.example.demo.Wordle.models;

import com.example.demo.Fiszki.models.FlashcardSet;
import com.example.demo.Hangman.models.HangmanGameStatus;
import com.example.demo.Login.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;

import java.util.*;

@JsonPropertyOrder({"gameID", "status", "guess", "letterCount", "guessesLeft", "hint", "notUsedLetters"})
@Entity
@Table(name = "wordle_games")
public class WordleGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="game_id")
    private int gameId;
    @Column(name="word",nullable = false)
    private String word;
    @Column(name="guess",nullable = false)
    private String guess;
    @Enumerated(EnumType.STRING)
    private WordleGameStatus status;
    @ElementCollection
    @MapKeyColumn(name = "letter")
    @Column(name = "state")
    @CollectionTable(name = "wordle_game_letter_states", joinColumns = @JoinColumn(name = "game_id"))
    @Enumerated(EnumType.STRING)
    private Map<Character, LetterState> letterStates;
    @Column(name = "guesses_left",nullable = false)
    @JsonProperty("guessesLeft")
    public int guessesLeft;
    @JsonIgnore
    private int letterCount;

    private static final int maxGuessCount = 6;

    @ManyToOne(cascade =
            {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name="user_id")
    private User user;

    public WordleGame() {
    }

    public WordleGame(FlashcardSet flashcardSet, String side, User user){
        this.word = getRandomWord(flashcardSet,side);
        this.guessesLeft = maxGuessCount;
        this.guess = getEmptyWord(this.word.length());
        this.letterStates = initializeLetterStates();
        this.letterCount = this.word.length();
        this.setStatus();
        this.user = user;
    }

    public int getGameID() {
        return gameId;
    }

    @JsonIgnore
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
    public int getGuesses_left() {
        return guessesLeft;
    }

    @JsonIgnore
    public int getMaxGuessCount() {
        return maxGuessCount;
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

        if (guess.length() != word.length()) {
            return;
        }

        updateLetterStates(guess);
        this.guess = guess;

        if (guess.equals(word)) {
            status = WordleGameStatus.WON;
        } else {
            guessesLeft--;
            if (guessesLeft == 0) {
                status = WordleGameStatus.LOST;
            }
        }

        setStatus();
    }

    private void updateLetterStates(String guess) {
        Set<Character> usedLetters = new HashSet<>();
        for (char c : guess.toCharArray()) {
            if (letterStates.containsKey(c)) {
                letterStates.put(c, LetterState.USED);
                usedLetters.add(c);
            }
        }
        for (char c : letterStates.keySet()) {
            if (!usedLetters.contains(c)) {
                letterStates.put(c, LetterState.NOT_USED);
            }
        }
    }




    public void setStatus(){
        if(word.equals(guess)) {
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
        updateLetterStates(guess);
    }

    private Map<Character, LetterState> initializeLetterStates() {
        Map<Character, LetterState> letterStates = new HashMap<>();

        for (char c = 'a'; c <= 'z'; c++) {
            letterStates.put(c, LetterState.NOT_USED);
        }

        return letterStates;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public void setGuessedWord(String guessedWord) {
        this.guess = guessedWord;
        this.setStatus();
    }

    @JsonProperty
    public int getLetterCount() {
        return letterCount;
    }
}