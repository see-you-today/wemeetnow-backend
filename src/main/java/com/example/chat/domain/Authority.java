package com.example.chat.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Authority implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "AUTHORITY_ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    private String role;

    public static Authority of(User user) {
        return Authority.builder()
                .role("ROLE_USER")
                .user(user)
                .build();
    }

    public static Authority ofAdmin(User user) {
        return Authority.builder()
                .role("ROLE_ADMIN")
                .user(user)
                .build();
    }

    @Override
    public String getAuthority() {
        return role;
    }
}
