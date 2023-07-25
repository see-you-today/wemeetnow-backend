package com.example.chat.controller;

import com.example.chat.domain.User;
import com.example.chat.dto.friend.FriendRequestDto;
import com.example.chat.service.AlarmWriteService;
import com.example.chat.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/alarms")
public class AlarmApiController {
    private final UserService userService;
    private final AlarmWriteService alarmWriteService;
    @PostMapping("/request-friend")
    public ResponseEntity requestFriend(HttpServletRequest request, @RequestBody FriendRequestDto requestDto) throws Exception {
        User loginedUser = userService.getUserFromTokenInRequest(request);
        alarmWriteService.makeFriendRequestAlarm(loginedUser, requestDto.getFriendedId());
        return new ResponseEntity(HttpStatus.OK);
    }
}
