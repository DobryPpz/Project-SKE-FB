package com.example.demo;

import com.example.demo.Hangman.models.HangmanGame;
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

}