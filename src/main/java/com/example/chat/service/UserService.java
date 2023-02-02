package com.example.chat.service;

import com.example.chat.domain.User;
import com.example.chat.dto.UserJoinDto;
import com.example.chat.exception.ApplicationException;
import com.example.chat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.chat.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService{
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional
    public User join(UserJoinDto joinDto) {
        String email = joinDto.getEmail();
        String password = joinDto.getPassword();
        String passwordCorrect = joinDto.getPasswordCorrect();

        userRepository.findByEmail(email).ifPresent((user) -> {
            throw new ApplicationException(DUPLICATED_EMAIL);
        });

        if(!passwordCorrect.equals(passwordCorrect)){
            throw new ApplicationException(INCORRECT_PASSWORD_CORRECT);
        }
        User user = joinDto.toEntity(passwordEncoder.encode(password));
        User savedUser = userRepository.save(user);
        log.info("savedUser = ", savedUser.toString());
        return savedUser.toDto();
    }
    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("일치하는 사용자가 존재하지 않습니다."));
    }
}