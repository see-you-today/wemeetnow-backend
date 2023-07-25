package com.example.chat.repository;

import com.example.chat.domain.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    @Query("select f from Friend f join fetch f.user u where u.id = :userId")
    List<Friend> findAllByUserId(Long userId);

}
