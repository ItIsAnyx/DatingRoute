package com.morzevichka.backend_api.repository;

import com.morzevichka.backend_api.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

   List<Chat> findAllByUserId(Long userId);
}
