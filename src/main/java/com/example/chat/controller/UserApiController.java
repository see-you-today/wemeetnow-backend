package com.example.chat.controller;

import com.example.chat.config.jwt.JwtTokenUtil;
import com.example.chat.dto.UserJoinDto;
import com.example.chat.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserApiController {
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    @GetMapping("/health")
    public String health() {
        return "OK";
    }

    @PostMapping("/join")
    public String join(@RequestBody UserJoinDto joinDto) {
        userService.join(joinDto);
        return "회원가입 완료";
    }

    @PostMapping("/join/admin")
    public String joinAdmin(@RequestBody UserJoinDto joinDto) {
        userService.joinAdmin(joinDto);
        return "어드민 회원 가입 완료";
    }
}
