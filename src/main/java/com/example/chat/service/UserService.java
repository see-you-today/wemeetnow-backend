package com.example.chat.service;

import com.example.chat.config.jwt.JwtUtil;
import com.example.chat.domain.User;
import com.example.chat.dto.UserJoinRequestDto;
import com.example.chat.dto.UserJoinResponseDto;
import com.example.chat.dto.UserLoginRequestDto;
import com.example.chat.dto.UserLoginResponseDto;
import com.example.chat.exception.ApplicationException;
import com.example.chat.repository.RefreshTokenRepository;
import com.example.chat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public UserJoinResponseDto join(UserJoinRequestDto joinRequestDto) {
        String email = joinRequestDto.getEmail();
        String password = joinRequestDto.getPassword();
        String passwordCorrect = joinRequestDto.getPasswordCorrect();

        userRepository.findByEmail(email).ifPresent((user) -> {
            throw new ApplicationException(DUPLICATED_EMAIL);
        });

        if(!passwordCorrect.equals(passwordCorrect)){
            throw new ApplicationException(INCORRECT_PASSWORD_CORRECT);
        }
        User user = joinRequestDto.toEntity(passwordEncoder.encode(password));
        User savedUser = userRepository.save(user);
        log.info("savedUser = ", savedUser);
        UserJoinResponseDto responseDto = UserJoinResponseDto.toDto(savedUser);
        return responseDto;
    }
    public UserLoginResponseDto login(UserLoginRequestDto loginRequestDto){
        User findUser = userRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(() -> new ApplicationException(USERNAME_NOT_FOUNDED));
        if(!passwordEncoder.matches(loginRequestDto.getPassword(), findUser.getPassword())) {
            throw new ApplicationException((INCORRECT_PASSWORD_CORRECT));
        }
        String accessToken = jwtUtil.generateAccessToken(findUser.getId(), findUser.getEmail(), findUser.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(findUser.getId(), findUser.getEmail(), findUser.getRole());

        refreshTokenRepository.save(refreshToken);

        UserLoginResponseDto responseDto = new UserLoginResponseDto(accessToken, refreshToken);
        return responseDto;
    }
    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("일치하는 사용자가 존재하지 않습니다."));
    }
}