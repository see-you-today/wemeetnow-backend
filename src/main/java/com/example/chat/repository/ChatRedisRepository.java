package com.example.chat.repository;

import com.example.chat.config.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static com.example.chat.config.jwt.JwtExpirationEnums.REFRESH_TOKEN_EXPIRATION_TIME;
/**
 * 채팅 내역을 관리하는 Repository클래스
 * */
@Repository
@RequiredArgsConstructor
@Slf4j
public class ChatRedisRepository {
    private final RedisTemplate redisTemplate;

    public void makeChatRoom(Long chatRoomId){
        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        ops.set(String.valueOf(chatRoomId), String.valueOf(new ArrayList<>())); // 초기 chatRoom value 값을 빈 리스트([])로 두어서 추후에 List<ChatDTO>를 관리할 수 있음
//        redisTemplate.expire(refreshToken, REFRESH_TOKEN_EXPIRATION_TIME.getValue(), TimeUnit.MILLISECONDS);
    }
}
