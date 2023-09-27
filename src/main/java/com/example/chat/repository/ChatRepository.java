package com.example.chat.repository;

import com.example.chat.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("select c from Chat c join fetch c.chatRoom cr where cr.id = :chatRoomId")
    List<Chat> findByChatRoomId(@Param("chatRoomId") Long chatRoomId);
}
