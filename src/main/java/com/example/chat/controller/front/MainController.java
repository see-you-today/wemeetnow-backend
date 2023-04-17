package com.example.chat.controller.front;

import com.example.chat.config.jwt.JwtUtil;
import com.example.chat.domain.User;
import com.example.chat.dto.UserLoginResponseDto;
import com.example.chat.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
@RequestMapping("/be")
@Slf4j
public class MainController {

    private final UserService userService;

    @GetMapping("")
    public String main(HttpServletRequest request, Model model, HttpServletResponse response) {
        log.info("request = ", request);
        log.info("request.toString() = ", request.toString());
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        Cookie[] cookies = request.getCookies();
        String cookieName = "";
        String cookieValue = "";
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookieName = cookie.getName();
                cookieValue = cookie.getValue();
                log.info("cName = [{}], cValue = [{}]", cookieName, cookieValue);
                if (cookieName == "accessToken") break;
            }
        }
        if (authorizationHeader != null) {
            String token = authorizationHeader.replace("Bearer ", "");
            String userEmail = JwtUtil.getEmail(token);
            User findUser = userService.getUserByEmail(userEmail);
            model.addAttribute("loginUser", findUser);
        }
        log.info("auth-header: [{}]", authorizationHeader);
        log.info("enter main()");
        return "main";
    }
}
