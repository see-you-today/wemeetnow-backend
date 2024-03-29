package com.example.chat.controller;

import com.example.chat.config.jwt.JwtUtil;
import com.example.chat.domain.User;
import com.example.chat.domain.enums.Role;
import com.example.chat.dto.*;
import com.example.chat.repository.RefreshTokenRepository;
import com.example.chat.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Slf4j
public class UserApiController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @GetMapping("/chat")
    public String chat() {
        return "OK";
    }

    @PostMapping("/join")
    public ResponseEntity<UserJoinResponseDto> join(@RequestBody UserJoinRequestDto requestDto) {
        UserJoinResponseDto responseDto = userService.join(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(@RequestBody UserLoginRequestDto requestDto){
        log.info("UserLoginRequestDto = [{}]", requestDto);
        UserLoginResponseDto responseDto = userService.login(requestDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseDto);
    }
    @GetMapping("/check-is-logined")
    public ResponseEntity checkIsLogined(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("request = " + request);
        try {
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            String token = authorizationHeader.replace("Bearer ", "");
            if (JwtUtil.isExpired(token)) {
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }
            Claims claims = JwtUtil.extractAllClaims(token);
            // JwtUtil.getEmail(token);
            log.info("claims.get(\"email\") = " + claims.get("email"));
            // JwtUtil.getId(token);
            log.info("claims.get(\"userId\") = " + claims.get("userId"));
            return new ResponseEntity(claims, HttpStatus.OK);
        } catch (Exception e) {
            log.error("raised error: ", e);
            throw new Exception(e);
        }
    }
    @GetMapping("/all")
    public ResponseEntity getUsersAll() {
        List<User> allUsers = userService.getAllUsers();
        return new ResponseEntity(allUsers, HttpStatus.OK);
    }

    @PostMapping("/reissue")
    public ResponseEntity reissue(@Valid @RequestBody RefreshTokenDto requestDto) throws Exception {
        if (JwtUtil.isExpired(requestDto.getRefreshToken())) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED); // 401
        }
        try {
            Claims claims = JwtUtil.extractAllClaims(requestDto.getRefreshToken());
            Long userId = Long.valueOf(String.valueOf(claims.get("userId")));
            String email = String.valueOf(claims.get("email"));
            Role userRole = Role.valueOf(String.valueOf(claims.get("role")));
            String newAccessToken = jwtUtil.generateAccessToken(userId, email, userRole);
            ReissueTokenResponseDto responseDto = new ReissueTokenResponseDto(newAccessToken, requestDto.getRefreshToken());
            return new ResponseEntity(responseDto, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("raised error: ", e);
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/logout")
    public ResponseEntity logout(@RequestBody RefreshTokenDto requestDto) {
        if (JwtUtil.isExpired(requestDto.getRefreshToken())) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        HttpStatus status = HttpStatus.NOT_IMPLEMENTED; // 501
        try {
            refreshTokenRepository.delete(requestDto.getRefreshToken());
            status = HttpStatus.NO_CONTENT;
        } catch (IllegalArgumentException e) {
            status = HttpStatus.BAD_REQUEST;
        } finally {
            return new ResponseEntity(status);
        }
    }
}
