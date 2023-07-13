package com.example.chat.domain;

import com.example.chat.dto.chat.ChatRoomCreateRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Table(name = "chat_room")
public class ChatRoom extends BaseTime {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;
    private String roomName;
    private int totalNum;
    private LocalDateTime lastMessageTime;
    private String chatRoomImg = "https://velog.velcdn.com/images/kyunghwan1207/post/298d6a8f-1a21-41d6-b550-69aad4473de1/image.png";
//    @JsonIgnore
    @OneToMany(mappedBy = "chatRoom")
    private List<Chat> chatList = new ArrayList<>();

//    @JsonIgnore
    @OneToMany(mappedBy = "chatRoom")
    private List<ChatParticipant> chatParticipantList = new ArrayList<>();


    public static ChatRoom create(ChatRoomCreateRequestDto chatRoomCreateRequestDto) {
        return ChatRoom.builder()
                .roomName(chatRoomCreateRequestDto.getChatRoomName())
                .totalNum(chatRoomCreateRequestDto.getParticipantIdList().size() + 1)
                .lastMessageTime(null)
                .chatList(new ArrayList<>())
                .chatParticipantList(new ArrayList<>())
                .build();
    }

    public static ChatRoom messageTimeUpdate(ChatRoom chatRoom) {
        return chatRoom.builder()
                .lastMessageTime(LocalDateTime.now())
                .build();
    }
}

