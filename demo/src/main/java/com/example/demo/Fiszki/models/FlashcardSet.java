package com.example.demo.Fiszki.models;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="flashcardsets")
public class FlashcardSet {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="name",nullable=false)
    private String name;

    @OneToMany(mappedBy = "flashcardSet",orphanRemoval = true)
    @JsonManagedReference
    private List<Flashcard> flashcards;

    @Column(name="user",nullable = false)
    private String username;

    public FlashcardSet(String name, String username) {
        this.name = name;
        this.flashcards = new ArrayList<>();
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Flashcard> getFlashcards() {
        return flashcards;
    }

    public void setFlashcards(List<Flashcard> flashcards) {
        this.flashcards = flashcards;
    }

    public Flashcard addFlashcard(Flashcard flashcard){
        this.flashcards.add(flashcard);
        return flashcard;
    }

    public void removeFlashcard(Flashcard flashcard){
        this.flashcards.remove(flashcard);
    }

    public void removeFlashcard(int id){
        this.flashcards.remove(id);
    }
    

}
