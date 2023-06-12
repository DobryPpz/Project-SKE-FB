package com.example.demo.Quiz.models;

import com.example.demo.Fiszki.models.Flashcard;
import com.example.demo.Fiszki.models.FlashcardSet;
import com.example.demo.Hangman.models.HangmanGameStatus;
import com.example.demo.Login.models.User;
import com.example.demo.Quiz.dto.QuizGameDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quiz_games")
public class QuizGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id")
    private int gameId;
    @Column(name = "front",nullable = false)
    private String currentFront;
    @Column(name = "back",nullable = false)
    private String back;
    @Enumerated(EnumType.STRING)
    private QuizGameStatus status;
    @Column(name = "points_won",nullable = false)
    private int pointsWon;
    @Column(name = "max_points",nullable = false)
    private int maxPoints;
    @ManyToOne(cascade =
            {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name="user_id")
    @JsonBackReference
    private User user;

    @ManyToOne(cascade =
            {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name="flashcardset_id")
    @JsonBackReference
    private FlashcardSet flashcardSet;

    @OneToMany
    @JoinColumn(name = "remaining_flashcards")
    @JsonManagedReference
    private List<Flashcard> remainingFlashcards;

    @OneToMany
    @JoinColumn(name = "used_flashcards")
    private List<Flashcard> usedFlashcards;

    public QuizGame(){}

    public QuizGame(FlashcardSet flashcardSet, User user){
        this.user = user;
        this.flashcardSet = flashcardSet;
        this.maxPoints = flashcardSet.getFlashcards().size();
        this.pointsWon = 0;
        this.remainingFlashcards = new ArrayList<>(flashcardSet.getFlashcards());
        this.usedFlashcards = new ArrayList<>();
        chooseRandomFlashcard();
        setStatus(QuizGameStatus.ACTIVE);
    }

    public QuizGameDTO check(String guess){
        if(getStatus()==QuizGameStatus.ACTIVE){
            QuizGameDTO ret = null;
            if(guess.equals(getBack())){
                setPointsWon(getPointsWon()+1);
                chooseRandomFlashcard();
                ret = new QuizGameDTO("correct!",getPointsWon(),getCurrentFront(),this.getGameId());
            }
            else{
                chooseRandomFlashcard();
                ret = new QuizGameDTO("wrong!",getPointsWon(),getCurrentFront(),this.getGameId());
            }
            return ret;
        }
        return new QuizGameDTO("game is already over",null,null,this.getGameId());
    }

    public void chooseRandomFlashcard(){
        if(remainingFlashcards.size()==0){
            setStatus(QuizGameStatus.OVER);
        }
        else{
            Flashcard gotFlashcard = remainingFlashcards.remove((int)(Math.random()*remainingFlashcards.size()));
            usedFlashcards.add(gotFlashcard);
            setCurrentFront(gotFlashcard.getFront());
            setBack(gotFlashcard.getBack());
        }
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getCurrentFront() {
        return currentFront;
    }

    public void setCurrentFront(String currentFront) {
        this.currentFront = currentFront;
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }

    public QuizGameStatus getStatus() {
        return status;
    }

    public void setStatus(QuizGameStatus status) {
        this.status = status;
    }

    public int getPointsWon() {
        return pointsWon;
    }

    public void setPointsWon(int pointsWon) {
        this.pointsWon = pointsWon;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public FlashcardSet getFlashcardSet() {
        return flashcardSet;
    }

    public void setFlashcardSet(FlashcardSet flashcardSet) {
        this.flashcardSet = flashcardSet;
    }

    public List<Flashcard> getRemainingFlashcards() {
        return remainingFlashcards;
    }

    public void setRemainingFlashcards(List<Flashcard> remainingFlashcards) {
        this.remainingFlashcards = remainingFlashcards;
    }

    public List<Flashcard> getUsedFlashcards() {
        return usedFlashcards;
    }

    public void setUsedFlashcards(List<Flashcard> usedFlashcards) {
        this.usedFlashcards = usedFlashcards;
    }
}
