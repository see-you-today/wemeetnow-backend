package com.example.chat.dto.chat;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Msg {
    private String sendid;
    private String receiveid;
    private String content1;
    private String content2;
}