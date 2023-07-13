package com.example.chat.service;

import com.example.chat.domain.Chat;
import com.example.chat.domain.ChatParticipant;
import com.example.chat.domain.ChatRoom;
import com.example.chat.domain.User;
import com.example.chat.dto.chat.ChatInviteRequestDto;
import com.example.chat.dto.chat.ChatRoomCreateRequestDto;
import com.example.chat.dto.chat.ChatRoomResponseDto;
import com.example.chat.repository.ChatParticipantRepository;
import com.example.chat.repository.ChatRoomRepository;
import com.example.chat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableAsync
public class ChatRoomService {
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatService chatService;

    /**
     * 채팅방 생성했을 때, 채팅방과 참가자의 양방향 연관관계까지 고려해서 처리
     */
    @Transactional
    public ChatRoom createChatRoom(ChatRoomCreateRequestDto chatRoomCreateRequestDto, Long loginedUserId) {
        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create(chatRoomCreateRequestDto));
        User loginedUser = userRepository.findById(loginedUserId).get();
        ChatParticipant chatParticipant1 = chatParticipantRepository.save(new ChatParticipant(chatRoom, loginedUser));

        loginedUser.getChatParticipantList().add(chatParticipant1);
        chatRoom.getChatParticipantList().add(chatParticipant1);
        chatParticipantRepository.save(chatParticipant1); // 자기자신을 참가자로 저장
        for (Long participantId : chatRoomCreateRequestDto.getParticipantIdList()) {
            User findUser = userRepository.findById(participantId).get();
            ChatParticipant chatParticipant = new ChatParticipant(chatRoom, findUser);
            findUser.getChatParticipantList().add(chatParticipant);
            chatRoom.getChatParticipantList().add(chatParticipant);
            chatParticipantRepository.save(chatParticipant); // 초대한 사람들 참가자로 저장
        }

        return chatRoom;
    }

    @Transactional
    public List<ChatParticipant> inviteMember(ChatInviteRequestDto chatInviteRequestDto) {
        Long chatRoomId = chatInviteRequestDto.getChatRoomId();
        //사용자 아이디로 참여한 사용자 리스트 찾기
        List<User> users = userRepository.findAllInUserIdList(chatInviteRequestDto.getUserIdList());

        //이미 방에 들어와있는지 확인
        for (User user : users) {
            Optional<ChatParticipant> findUser = chatParticipantRepository.findByChatRoomIdAndUserId(chatRoomId, user.getId());
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

    @Transactional(readOnly = true)
    public List<ChatRoomResponseDto> getChatRooms(Long loginedUserId) {
        try {
            List<ChatRoomResponseDto> chatRoomResponseDtoList = new ArrayList<>();
            List<ChatRoom> chatRoomList = chatRoomRepository.findAllWithUserId(loginedUserId);
            // 각 ChatRoom의 Chat 중 맨 마지막 lastMessage, lastDateTime 조회해서 fromEntity에 param으로 전달
            for (ChatRoom chatRoom : chatRoomList) {
                int chatListSize = chatRoom.getChatList().size();
                if (chatListSize > 0) {
                    Chat chat = chatRoom.getChatList().get(chatListSize - 1);
                    chatRoomResponseDtoList.add(ChatRoomResponseDto.fromEntityWithChat(chatRoom, chat));
                } else {
                    chatRoomResponseDtoList.add(ChatRoomResponseDto.fromEntityNoChat(chatRoom));
                }
            }
            return chatRoomResponseDtoList;

        } catch (Exception e) {
            e.printStackTrace();
            e.getStackTrace();
            throw new IllegalArgumentException("채팅방 목록을 조회할 수 없습니다.");
        }
    }
}

