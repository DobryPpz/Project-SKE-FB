package com.example.demo.Login.models;

import com.example.demo.Hangman.models.HangmanGame;
import com.example.demo.Quiz.models.QuizGame;
import com.example.demo.Wordle.models.WordleGame;
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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user",cascade =
            {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    List<HangmanGame> hangmanGames;

    @OneToMany(mappedBy = "user",cascade =
            {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    List<WordleGame> wordleGames;
    @OneToMany(mappedBy = "user",cascade =
            {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    List<QuizGame> quizGames;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    private List<Role> roles = new ArrayList<>();
    private boolean isEnabled;
    public User(String username, String email, String password, List<Role> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public List<HangmanGame> getHangmanGames() {
        return hangmanGames;
    }

    public void setHangmanGames(List<HangmanGame> hangmanGames) {
        this.hangmanGames = hangmanGames;
    }

    public void addHangmanGame(HangmanGame hangmanGame){
        if(hangmanGames==null)
            hangmanGames=new ArrayList<>();
        hangmanGames.add(hangmanGame);
        hangmanGame.setUser(this);
    }


    public List<WordleGame> getWordleGames() {
        return wordleGames;
    }

    public void setWordleGames(List<WordleGame> wordleGames) {
        this.wordleGames = wordleGames;
    }

    public void addWordleGame(WordleGame wordleGame) {
        if(wordleGames==null)
            wordleGames=new ArrayList<>();
        wordleGames.add(wordleGame);
        wordleGame.setUser(this);
    }

    public void addQuizGame(QuizGame quizGame){
        if(quizGames==null){
            quizGames = new ArrayList<>();
        }
        quizGames.add(quizGame);
        quizGame.setUser(this);
    }
}