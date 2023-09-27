package com.example.chat.repository;

import com.example.chat.domain.ChatRoom;
import com.example.chat.dto.chat.ChatRoomResponseDto;
import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query(value = "select cr from ChatRoom cr join fetch cr.chatParticipantList cp where cp.user.id = :userId")
    List<ChatRoom> findAllWithUserId(@Param("userId") Long userId);
}
