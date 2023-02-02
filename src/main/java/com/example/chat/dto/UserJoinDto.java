package com.example.chat.dto;

import com.example.chat.domain.Role;
import com.example.chat.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class UserJoinDto {
    private String email;
    private String password;
    private String passwordCorrect;
    private String nickname;
    private String username;
    private Role role;

    public User toEntity(String enCodedPassword) {
        List<Role> roleList = new ArrayList<>();
        roleList.add(this.role);
        return User.builder()
                .email(this.email)
                .password(enCodedPassword)
                .username(this.username)
                .roles(roleList)
                .build();
    }
}
