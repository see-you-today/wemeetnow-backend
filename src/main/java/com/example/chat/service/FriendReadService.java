package com.example.chat.service;

import com.example.chat.domain.Friend;
import com.example.chat.domain.User;
import com.example.chat.dto.friend.FriendResponseDto;
import com.example.chat.repository.FriendRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendReadService {
    private final FriendRepository friendRepository;
    private final UserService userService;

    public List<FriendResponseDto> getFriendListDtoFromUserId(Long loginedUserId) {
        try {
            List<FriendResponseDto> friendListResponseDtoList = new ArrayList<>();
            List<Friend> allWithUserId = friendRepository.findAllByUserId(loginedUserId);
            for (Friend friend : allWithUserId) {
                User findUser = userService.getUserById(friend.getFriendedId());
                friendListResponseDtoList.add(FriendResponseDto.toDto(findUser));
            }
            return friendListResponseDtoList;
        } catch (Exception e) {
            log.error(e + " | 친구목록 조회에 실패했습니다.");
            return null;
        }
    }
}
