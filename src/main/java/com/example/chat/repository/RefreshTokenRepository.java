package com.example.chat.repository;

import com.example.chat.config.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.example.chat.config.jwt.JwtExpirationEnums.REFRESH_TOKEN_EXPIRATION_TIME;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenRepository {
    private final RedisTemplate redisTemplate;

    public void save(String refreshToken){
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        Long userId = JwtUtil.getId(refreshToken);
        ops.set(refreshToken, String.valueOf(userId));
        redisTemplate.expire(refreshToken, REFRESH_TOKEN_EXPIRATION_TIME.getValue(), TimeUnit.MILLISECONDS);
    }
    public void delete(String refreshToken) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        RedisOperations<String, String> operations = ops.getOperations();
        boolean isSuccessToDelete = operations.delete(refreshToken);
        if (!isSuccessToDelete) {
            throw new IllegalArgumentException("fail to logout handle");
        }
    }
}
