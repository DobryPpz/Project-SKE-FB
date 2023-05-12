package com.example.demo.Fiszki.models;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="flashcards")
public class Flashcard {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="front")
    private String front;

    @Column(name="back")
    private String back;

    @ManyToOne
    @JoinColumn(name="flashcardset_id",nullable = false)
    private FlashcardSet flashcardSet;

    public Flashcard(String front, String back, FlashcardSet flashcardSet) {
        this.front = front;
        this.back = back;
        this.flashcardSet = flashcardSet;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }
}
