package com.example.chat.controller;

import com.example.chat.domain.User;
import com.example.chat.dto.friend.FriendResponseDto;
import com.example.chat.dto.friend.FriendResponseDtoList;
import com.example.chat.service.FriendReadService;
import com.example.chat.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/friends")
public class FriendApiController {
    private final UserService userService;
    private final FriendReadService friendReadService;
    @GetMapping
    public ResponseEntity getFriendList(HttpServletRequest request) {
        try {
            User loginedUser = userService.getUserFromTokenInRequest(request);
            List<FriendResponseDto> responseDtoList = friendReadService.getFriendListDtoFromUserId(loginedUser.getId());
            FriendResponseDtoList response = new FriendResponseDtoList(responseDtoList);
            return new ResponseEntity(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e + "| 친구목록 조히에 실패했습니다.");
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
