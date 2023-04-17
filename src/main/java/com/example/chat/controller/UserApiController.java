package com.example.chat.controller;

import com.example.chat.config.jwt.JwtUtil;
import com.example.chat.domain.result.Response;
import com.example.chat.dto.UserJoinRequestDto;
import com.example.chat.dto.UserJoinResponseDto;
import com.example.chat.dto.UserLoginRequestDto;
import com.example.chat.dto.UserLoginResponseDto;
import com.example.chat.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Slf4j
public class UserApiController {
    private final UserService userService;

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
    public ResponseEntity<UserLoginResponseDto> login(@ModelAttribute UserLoginRequestDto requestDto){
        log.info("UserLoginRequestDto = [{}]", requestDto);
        UserLoginResponseDto responseDto = userService.login(requestDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseDto);
    }
    @GetMapping("/check-is-logined")
    public ResponseEntity checkIsLogined(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("request = " + request);
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = authorizationHeader.replace("Bearer ", "");
        Claims claims = JwtUtil.extractAllClaims(token);
        // JwtUtil.getEmail(token);
        System.out.println("claims.get(\"email\") = " + claims.get("email"));
        // JwtUtil.getId(token);
        System.out.println("claims.get(\"userId\") = " + claims.get("userId"));
        HttpStatus status;
        if (!JwtUtil.isExpired(token)) {
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.UNAUTHORIZED;
        }
        return new ResponseEntity(status);
    }
}
