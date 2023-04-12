package com.example.chat.repository;

import com.example.chat.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    @Query("select u from User u where u.id in :userIdList")
    List<User> findAllInUserIdList(List<Long> userIdList);

}
