package com.example.chat.domain;

import com.example.chat.domain.enums.ChatType;
import com.example.chat.dto.chat.ChatRequestDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;

    private String message;
    private ChatType chatType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static Chat save(ChatRequestDto chatRequestDto, ChatRoom chatRoom, User user) {
        return Chat.builder()
                .message(chatRequestDto.getMessage())
                .chatType(chatRequestDto.getChatType())
                .chatRoom(chatRoom)
                .user(user)
                .build();
    }

}
