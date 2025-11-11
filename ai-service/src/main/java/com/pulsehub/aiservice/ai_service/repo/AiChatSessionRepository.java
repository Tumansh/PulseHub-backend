package com.pulsehub.aiservice.ai_service.repo;

import com.pulsehub.aiservice.ai_service.model.AiChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AiChatSessionRepository extends JpaRepository<AiChatSession, Long> {
    List<AiChatSession> findByUserIdOrderByCreatedAtDesc(Long userId);
}
