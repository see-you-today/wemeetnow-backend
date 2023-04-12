package com.example.chat.repository;

import com.example.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("select cr from ChatRoom cr, ChatParticipant cp where cp.user_id=:userId and cp.chat_room_id=cr.id")
    List<ChatRoom> findAllWithUserId(Long userId);
}
