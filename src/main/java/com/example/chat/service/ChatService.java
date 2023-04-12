package com.example.chat.service;

import com.example.chat.domain.ChatParticipant;
import com.example.chat.domain.ChatRoom;
import com.example.chat.domain.User;
import com.example.chat.dto.chat.ChatInviteRequestDto;
import com.example.chat.dto.chat.ChatRoomRequestDto;
import com.example.chat.repository.ChatParticipantRepository;
import com.example.chat.repository.ChatRoomRepository;
import com.example.chat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableAsync
public class ChatService {
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    private final ChatParticipantRepository chatParticipantRepository;

    public ChatRoom createChatRoom(ChatRoomRequestDto chatRoomRequestDto) {
        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.save(chatRoomRequestDto));
        return chatRoom;
    }

    public List<ChatParticipant> inviteMember(ChatInviteRequestDto chatInviteRequestDto) {
        Long chatRoomId = chatInviteRequestDto.getChatRoomId();
        //사용자 아이디로 참여한 사용자 리스트 찾기
        List<User> users = userRepository.findAllInUserIdList(chatInviteRequestDto.getUserIdList());

        //이미 방에 들어와있는지 확인
        for (User user : users) {
            Optional<ChatParticipant> findUser = chatParticipantRepository.findByChatRoom_IdAndUser_Id(chatRoomId, user.getId());
            if (findUser.isPresent()) {
                users.remove(user);
            }
        }
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new RuntimeException());

        // 해당 사용자와 방 번호를 넣은 chatParticipant list 생성 후 저장
        List<ChatParticipant> chatParticipants =
                users.stream().map(
                        user -> ChatParticipant.builder()
                                .chatRoom(chatRoom)
                                .user(user).build()
                        ).collect(Collectors.toList());

        chatParticipantRepository.saveAll(chatParticipants);

        return chatParticipants;
    }
}

