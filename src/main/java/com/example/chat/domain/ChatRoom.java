package com.example.chat.domain;

import com.example.chat.dto.chat.ChatRoomCreateRequestDto;
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
    private String chatRoomName;
    private int totalNum;
    private LocalDateTime lastMessageTime;
    private String chatRoomImgUrl;
//    @JsonIgnore
    @OneToMany(mappedBy = "chatRoom")
    private List<Chat> chatList = new ArrayList<>();

//    @JsonIgnore
    @OneToMany(mappedBy = "chatRoom")
    private List<ChatParticipant> chatParticipantList = new ArrayList<>();


    public static ChatRoom create(ChatRoomCreateRequestDto chatRoomCreateRequestDto) {
        String chatRoomImgUrl = "https://velog.velcdn.com/images/kyunghwan1207/post/28ea6b20-85ba-476f-b6b6-74e460ec8382/image.png"; // 채팅방 default 이미지 url
        if (!chatRoomCreateRequestDto.getChatRoomImgUrl().isEmpty()) {
            chatRoomImgUrl = chatRoomCreateRequestDto.getChatRoomImgUrl();
        }
        return ChatRoom.builder()
                .chatRoomName(chatRoomCreateRequestDto.getChatRoomName())
                .totalNum(chatRoomCreateRequestDto.getParticipantIdList().size() + 1)
                .lastMessageTime(null)
                .chatList(new ArrayList<>())
                .chatParticipantList(new ArrayList<>())
                .chatRoomImgUrl(chatRoomImgUrl)
                .build();
    }

    public static ChatRoom messageTimeUpdate(ChatRoom chatRoom) {
        return chatRoom.builder()
                .lastMessageTime(LocalDateTime.now())
                .build();
    }
}

