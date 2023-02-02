package com.example.chat.config.jwt;

import com.example.chat.domain.User;
import com.example.chat.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpHeaders.*;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // token은 변경되면 안되기에 final키워드 사용
        final String authorizationHeader = request.getHeader(AUTHORIZATION);
        log.info("authorizatonHeader: {}", authorizationHeader);

        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        // token 분리
        String token;
        try {
            token = authorizationHeader.split(" ")[1];
        } catch (Exception e){
            log.error("toekn 추출에 실패했습니다");
            filterChain.doFilter(request, response);
            return;
        }
        // token 유효한지 check
        if(JwtUtil.isExpired(token)){
            log.info("token이 유효하지 않습니다");
            filterChain.doFilter(request, response);
            return;
        }
        String email = JwtUtil.getEmail(token);
        log.info("try access user's email = " + email);
        User findUser = userService.getUserByEmail(email);
        log.info("findUser's role = " + findUser.getRoles());

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("", null, List.of(new SimpleGrantedAuthority("ROLE_USER"))); // 문열어주기
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // 다음인증에 필요한 정보를 넘김
        SecurityContextHolder.getContext().setAuthentication(authenticationToken); // 권한 부여
        filterChain.doFilter(request, response);
    }
}
