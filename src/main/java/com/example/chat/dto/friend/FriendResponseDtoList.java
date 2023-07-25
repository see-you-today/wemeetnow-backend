package com.example.chat.dto.friend;

import com.example.chat.domain.Friend;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendResponseDtoList {
    List<FriendResponseDto> friendDtoList;
}
