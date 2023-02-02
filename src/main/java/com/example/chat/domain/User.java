package com.example.chat.domain;

import lombok.*;

import javax.persistence.*;

import java.util.*;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(unique = true)
    private String nickname;
    private String provider;
    private Boolean emailAuth;

    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    private List<Role> roles = new ArrayList<>();


    @Builder
    public User(String username, String email, String password, String nickname, String provider, Boolean emailAuth, List<Role> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.provider = provider;
        this.emailAuth = emailAuth;
        this.roles = roles;
    }

    public void addRole(Role role){
        this.roles.add(role);
    }
    public void emailVerifiedSuccess(){
        this.emailAuth = true;
    }

    public User toDto() {
        return User.builder()
                .email(this.email)
                .password(this.password)
                .username(this.username)
                .roles(this.roles)
                .build();
    }
}
