package com.example.chat.controller.front;

import com.example.chat.config.jwt.JwtUtil;
import com.example.chat.domain.User;
import com.example.chat.dto.ChatSendRequestDto;
import com.example.chat.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatApiFrontController {

    private final UserService userService;

    @GetMapping("/chat")
    public String chat(HttpServletRequest request, Model model) {
        log.info("request's cookies = " + request.getCookies());
        Cookie[] cookies = request.getCookies();
        String cookieName = "";
        String cookieValue = "";
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookieName = cookie.getName();
                cookieValue = cookie.getValue();
                log.info("cookieName = [{}], cookieValue = [{}]", cookieName, cookieValue);
                if (cookieName == "accessToken") break;
            }
        }
        if (!cookieName.isBlank()) {
            String token = cookieValue;
        }

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        Long senderId = 1L;
        User findUser = null;
        if (authorizationHeader != null) {
            log.info("auth header is not null");
            String token = authorizationHeader.replace("Bearer ", "");
            log.info("token = [{}]", token);
            senderId = JwtUtil.getId(token);
            log.info("senderId = [{}]", senderId);
            findUser = userService.getUserById(senderId);
        } else {
            log.info("auth hedaer is null");
        }
        model.addAttribute("senderId", senderId);
        model.addAttribute("loginUser", findUser);
        model.addAttribute("center", "chat/chat");
        return "main";
    }

}
