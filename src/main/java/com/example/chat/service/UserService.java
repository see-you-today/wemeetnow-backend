package com.example.chat.service;

import com.example.chat.config.jwt.JwtTokenUtil;
import com.example.chat.domain.User;
import com.example.chat.dto.UserJoinDto;
import com.example.chat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void join(UserJoinDto joinDto) {
        joinDto.setPassword(passwordEncoder.encode(joinDto.getPassword()));
        userRepository.save(User.of(joinDto));
    }

    public void joinAdmin(UserJoinDto joinDto) {
        joinDto.setPassword(passwordEncoder.encode(joinDto.getPassword()));
        userRepository.save(User.ofAdmin(joinDto));
    }
}
