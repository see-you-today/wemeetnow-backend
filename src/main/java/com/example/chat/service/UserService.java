package com.example.chat.service;

import com.example.chat.config.cache.CacheKey;
import com.example.chat.config.jwt.JwtExpirationEnums;
import com.example.chat.config.jwt.JwtTokenUtil;
import com.example.chat.domain.LogoutAccessToken;
import com.example.chat.domain.RefreshToken;
import com.example.chat.domain.User;
import com.example.chat.dto.LoginDto;
import com.example.chat.dto.TokenDto;
import com.example.chat.dto.UserInfoDto;
import com.example.chat.dto.JoinDto;
import com.example.chat.repository.LogoutAccessTokenRedisRepository;
import com.example.chat.repository.RefreshTokenRedisRepository;
import com.example.chat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static com.example.chat.config.jwt.JwtExpirationEnums.REFRESH_TOKEN_EXPIRATION_TIME;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;
    private final JwtTokenUtil jwtTokenUtil;

    public void join(JoinDto joinDto) {
        joinDto.setPassword(passwordEncoder.encode(joinDto.getPassword()));
        userRepository.save(User.of(joinDto));
    }

    public void joinAdmin(JoinDto joinDto) {
        joinDto.setPassword(passwordEncoder.encode(joinDto.getPassword()));
        userRepository.save(User.ofAdmin(joinDto));
    }
    // 1
    public TokenDto login(LoginDto loginDto) {
        log.info("[US] login()/loginDto.toString() = " + loginDto.toString());
        User user = userRepository.findByEmail(loginDto.getEmail()).orElseThrow(() -> new NoSuchElementException("[US] login()/존재하지 않는 사용자입니다."));
        log.info("[US] login()/user.toString() = " + user.toString());
        checkPassword(loginDto.getPassword(), user.getPassword());

        String username = user.getUsername();
        String accessToken = jwtTokenUtil.generateAccessToken(username);
        log.info("[US] login()/accessToken = " + accessToken);
        RefreshToken refreshToken = saveRefreshToken(username);
        log.info("[US] login()/refreshToken = " + refreshToken);
        return TokenDto.of(accessToken, refreshToken.getRefreshToken());
    }

    private void checkPassword(String rawPassword, String findUserPassword) {
        log.info(String.format("[US] checkPassword()/ rawPw: {}, findUserPw: {}", rawPassword, findUserPassword));
        if (!passwordEncoder.matches(rawPassword, findUserPassword)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    private RefreshToken saveRefreshToken(String username) {
        log.info("[US] saveRefreshToken()/username = " + username);
        Iterable<RefreshToken> refreshTokenRedisRepositoryAll = refreshTokenRedisRepository.findAll();
        if(refreshTokenRedisRepositoryAll != null){
            for (RefreshToken refreshToken : refreshTokenRedisRepositoryAll) {
                log.info("[US] saveRefreshToken()/refreshToke = " + refreshToken.toString());
            }
        } else {
            log.info("[US] saveRefreshToken()/refreshToke = null");
        }

        return refreshTokenRedisRepository.save(RefreshToken.createRefreshToken(username,
                jwtTokenUtil.generateRefreshToken(username), REFRESH_TOKEN_EXPIRATION_TIME.getValue()));
    }

    // 2
    public UserInfoDto getUserInfo(String email) {
        log.info("[US] getUserInfo()/email = " + email);
        List<User> all = userRepository.findAll();
        for (User user : all) {
            System.out.println("user.toString() = " + user.toString());
        }
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("[US] getUserInfo()/존재하지 않는 사용자입니다."));
        log.info("[US] getUserInfo()/user.toString() = " + user.toString());
        if (!user.getUsername().equals(getCurrentUsername())) {
            throw new IllegalArgumentException("사용자 정보가 일치하지 않습니다.");
        }
        return UserInfoDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    // 4
    @CacheEvict(value = CacheKey.USER, key = "#username")
    public void logout(TokenDto tokenDto, String username) {
        String accessToken = resolveToken(tokenDto.getAccessToken());
        long remainMilliSeconds = jwtTokenUtil.getRemainMilliSeconds(accessToken);
        refreshTokenRedisRepository.deleteById(username);
        logoutAccessTokenRedisRepository.save(LogoutAccessToken.of(accessToken, username, remainMilliSeconds));
    }

    private String resolveToken(String token) {
        return token.substring(7);
    }

    // 3
    public TokenDto reissue(String refreshToken) {
        refreshToken = resolveToken(refreshToken);
        String username = getCurrentUsername();
        RefreshToken redisRefreshToken = refreshTokenRedisRepository.findById(username).orElseThrow(NoSuchElementException::new);

        if (refreshToken.equals(redisRefreshToken.getRefreshToken())) {
            return reissueRefreshToken(refreshToken, username);
        }
        throw new IllegalArgumentException("토큰이 일치하지 않습니다.");
    }

    private String getCurrentUsername() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        return principal.getUsername();
    }

    private TokenDto reissueRefreshToken(String refreshToken, String username) {
        if (lessThanReissueExpirationTimesLeft(refreshToken)) {
            String accessToken = jwtTokenUtil.generateAccessToken(username);
            return TokenDto.of(accessToken, saveRefreshToken(username).getRefreshToken());
        }
        return TokenDto.of(jwtTokenUtil.generateAccessToken(username), refreshToken);
    }

    private boolean lessThanReissueExpirationTimesLeft(String refreshToken) {
        return jwtTokenUtil.getRemainMilliSeconds(refreshToken) < JwtExpirationEnums.REISSUE_EXPIRATION_TIME.getValue();
    }
}
