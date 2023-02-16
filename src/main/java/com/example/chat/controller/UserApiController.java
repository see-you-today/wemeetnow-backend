package com.example.chat.controller;

import com.example.chat.config.jwt.JwtUtil;
import com.example.chat.domain.result.Response;
import com.example.chat.dto.UserJoinRequestDto;
import com.example.chat.dto.UserJoinResponseDto;
import com.example.chat.dto.UserLoginRequestDto;
import com.example.chat.dto.UserLoginResponseDto;
import com.example.chat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserApiController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

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
        UserLoginResponseDto responseDto = userService.login(requestDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseDto);
    }
}
