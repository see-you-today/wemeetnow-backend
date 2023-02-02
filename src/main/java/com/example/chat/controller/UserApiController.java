package com.example.chat.controller;

import com.example.chat.config.jwt.JwtUtil;
import com.example.chat.dto.UserJoinDto;
import com.example.chat.service.UserService;
import lombok.RequiredArgsConstructor;
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
    public String join(@RequestBody UserJoinDto userJoinDto) {
        userService.join(userJoinDto);
        return "회원가입 완료";
    }
}
