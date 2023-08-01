package com.example.chat.domain;

import com.example.chat.domain.enums.ChatType;
import com.example.chat.dto.ChatSendRequestDto;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Table(name = "chat")
public class Chat extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;

    private String message;
    @Enumerated(EnumType.STRING)
    private ChatType chatType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private int notReadCount;
    @Nullable
    private String invitedUserName;


    public static Chat createChat(User findUser, ChatRoom chatRoom, ChatSendRequestDto requestDto) {
        return Chat.builder()
                .user(findUser)
                .chatRoom(chatRoom)
                .message(requestDto.getContent())
                .chatType(requestDto.getChatType())
                .notReadCount(chatRoom.getTotalNum())
                .build();
    }
}
