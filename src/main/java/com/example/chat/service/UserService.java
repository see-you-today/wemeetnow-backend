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
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.codehaus.groovy.syntax.TokenException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

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
        // expiration date 까지 설정해서 token return 함
        String accessToken = jwtUtil.generateAccessToken(findUser.getId(), findUser.getEmail(), findUser.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(findUser.getId(), findUser.getEmail(), findUser.getRole());

        refreshTokenRepository.save(refreshToken);

        UserLoginResponseDto responseDto = new UserLoginResponseDto(accessToken, refreshToken);
        return responseDto;
    }
    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("일치하는 사용자 이메일이 없습니다."));
    }
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("일치하는 사용자 id가 없습니다."));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public Long getUserIdFromTokenInRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = authorizationHeader.replace("Bearer ", "");

        Long retValue = 0L;
        if (!JwtUtil.isExpired(token)) {
            retValue = JwtUtil.getId(token);
        }
        return retValue;
    }
    public User getUserFromTokenInRequest(HttpServletRequest request) throws Exception {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = authorizationHeader.replace("Bearer ", "");
        if (JwtUtil.isExpired(token)) {
            log.error("토큰이 유효하지 않습니다.");
            throw new Exception("토큰이 유효하지 않습니다.");
        }
        Optional<User> findUser = userRepository.findById(JwtUtil.getId(token));
        if (findUser.isPresent()) {
            return findUser.get();
        } else {
            log.error("사용자가 존재하지 않습니다.");
            throw new Exception("사용자가 존재하지 않습니다.");
        }
    }
}