package com.example.chat.repository;

import com.example.chat.domain.LogoutAccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogoutAccessTokenRedisRepository extends JpaRepository<LogoutAccessToken, String> {
}
