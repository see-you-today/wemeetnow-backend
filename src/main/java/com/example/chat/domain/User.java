package com.example.chat.domain;

import com.example.chat.dto.UserJoinDto;
import lombok.*;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(unique = true)
    private String nickname;

    @OneToMany(mappedBy = "user", cascade = ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Authority> authorities = new HashSet<>();

    public static User of(UserJoinDto joinDto) {
        User user = User.builder()
                .username(UUID.randomUUID().toString())
                .email(joinDto.getEmail())
                .password(joinDto.getPassword())
                .nickname(joinDto.getNickname())
                .build();
        user.addAuthority(Authority.of(user));
        return user;
    }

    public static User ofAdmin(UserJoinDto joinDto) {
        User user = User.builder()
                .username(UUID.randomUUID().toString())
                .email(joinDto.getEmail())
                .password(joinDto.getPassword())
                .nickname(joinDto.getNickname())
                .build();
        user.addAuthority(Authority.ofAdmin(user));
        return user;
    }

    private void addAuthority(Authority authority) {
        authorities.add(authority);
    }

    public List<String> getRoles() {
        return authorities.stream()
                .map(Authority::getRole)
                .collect(toList());
    }
}
