package com.example.demo.Wordle.models;
import java.util.List;
import java.util.Scanner;
import java.util.*;

public class WordleGame {
    private final int gameId;
    private final String wordToGuess;
    private String guess;
    private int guessCount;
    private final int maxGuessCount = 6;
    private WordleGameStatus status;

    public WordleGame(List<String> listOfWords, int gameId) {
        this.gameId = gameId;
        Random random = new Random();
        this.wordToGuess = listOfWords.get(random.nextInt(listOfWords.size()));
        this.guess = "";
        this.guessCount = 0;
        this.status = WordleGameStatus.ACTIVE;
    }

    public int getGameId() {
        return gameId;
    }

    public String getWordToGuess() {
        return wordToGuess;
    }

    public String getGuess() {
        return guess;
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
        for (char c : word.toCharArray()) {
            unsolvedLetters.add(c);
        }

        //dodaje odgadniete do guessed
        for (char c : guessedWords.toString().toCharArray()) {
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

        for (int i = 0; i < wordToGuess.length(); i++) {
            char c = wordToGuess.charAt(i);
            if (guess.charAt(i) == c) {
                correctChars.add(c);
            } else if (guess.contains(Character.toString(c))) {
                incorrectChars.add(c);
            }
        }

        if (correctChars.size() == wordToGuess.length()) {
            status = WordleGameStatus.WON;
        } else if (guessCount >= maxGuessCount) {
            status = WordleGameStatus.LOST;
        }

        if (status == WordleGameStatus.ACTIVE) {
            status = WordleGameStatus.PLAYING;
        }
    }

    public class WordleGame {

        public static void main(String[] args) {
            List<String> words = TempClassForWords.getWords();
            int gameId = 1;
            WordleGame game = new WordleGame(words, gameId);
            Scanner scanner = new Scanner(System.in);

            while (game.getStatus() == WordleGameStatus.ACTIVE) {
                System.out.println("Guess the word: " + game.getWordState());
                String guess = scanner.nextLine();
                game.checkGuess(guess);
                System.out.println(game.getFeedback());
            }

            if (game.getStatus() == WordleGameStatus.WON) {
                System.out.println("Congratulations! You won!");
            } else {
                System.out.println("Sorry, you lost. The word was " + game.getWord());
            }
        }
}