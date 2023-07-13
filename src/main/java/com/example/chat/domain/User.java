package com.example.chat.domain;

import com.example.chat.domain.enums.Role;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
@ToString
@Table(name = "user")
public class User extends BaseTime {
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
    private String imgUrl = "https://velog.velcdn.com/images/kyunghwan1207/post/ce34e29d-643a-4d52-8c1f-6f55232294c7/image.png";

    @Enumerated(EnumType.STRING)
    private Role role;

    @Nullable
    private String phoneNumber;

    @OneToMany(mappedBy = "user")
    private List<Friend> friendList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<ChatParticipant> chatParticipantList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Chat> chatList = new ArrayList<>();

    @Builder
    public User(String username, String email, String password, String nickname, String provider, Boolean emailAuth, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.provider = provider;
        this.emailAuth = emailAuth;
        this.role = role;
    }

    public void emailVerifiedSuccess(){
        this.emailAuth = true;
    }

    public User toDto() {
        return User.builder()
                .email(this.email)
                .password(this.password)
                .username(this.username)
                .role(this.role)
                .build();
    }
}
