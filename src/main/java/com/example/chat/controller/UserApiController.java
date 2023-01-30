package com.example.chat.controller;

import com.example.chat.config.jwt.JwtTokenUtil;
import com.example.chat.dto.LoginDto;
import com.example.chat.dto.TokenDto;
import com.example.chat.dto.UserInfoDto;
import com.example.chat.dto.JoinDto;
import com.example.chat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserApiController {
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    @GetMapping("/chat")
    public String chat() {
        return "OK";
    }

    @PostMapping("/join")
    public String join(@RequestBody JoinDto joinDto) {
        userService.join(joinDto);
        return "회원가입 완료";
    }

    @PostMapping("/join/admin")
    public String joinAdmin(@RequestBody JoinDto joinDto) {
        userService.joinAdmin(joinDto);
        return "어드민 회원 가입 완료";
    }
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(userService.login(loginDto));
    }

    @GetMapping("/users/{email}")
    public UserInfoDto getMemberInfo(@PathVariable String email) {
        return userService.getUserInfo(email);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestHeader("RefreshToken") String refreshToken) {
        return ResponseEntity.ok(userService.reissue(refreshToken));
    }

    @PostMapping("/logout")
    public void logout(@RequestHeader("Authorization") String accessToken,
                       @RequestHeader("RefreshToken") String refreshToken) {
        String username = jwtTokenUtil.getUsername(resolveToken(accessToken));
        userService.logout(TokenDto.of(accessToken, refreshToken), username);
    }

    private String resolveToken(String accessToken) {
        return accessToken.substring(7);
    }
}
