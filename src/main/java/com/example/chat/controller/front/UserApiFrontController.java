package com.example.chat.controller.front;

import com.example.chat.config.jwt.JwtUtil;
import com.example.chat.controller.UserApiController;
import com.example.chat.domain.User;
import com.example.chat.dto.UserLoginRequestDto;
import com.example.chat.dto.UserLoginResponseDto;
import com.example.chat.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/api-front/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserApiFrontController {
    private final UserService userService;

    @PostMapping("/login")
    public String loginFront(@ModelAttribute UserLoginRequestDto requestDto, Model model, HttpServletResponse response) {
        log.info("UserLoginRequestDto = ", requestDto);
        User loginUser = null;
        String accessToken = "";
        try {
            UserLoginResponseDto responseDto = userService.login(requestDto);
            accessToken = responseDto.getAccessToken();
            loginUser = userService.getUserById(JwtUtil.getId(accessToken));
            response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
            Cookie cookie = new Cookie("accessToken", accessToken);
            response.addCookie(cookie);
        } catch (Exception e) {
            e.getMessage();
            e.getStackTrace();
        } finally {
            model.addAttribute("loginUser", loginUser);

        }
        return "main";
    }
}
