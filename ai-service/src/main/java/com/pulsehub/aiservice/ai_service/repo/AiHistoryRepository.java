package com.pulsehub.aiservice.ai_service.repo;

import com.pulsehub.aiservice.ai_service.model.AiHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AiHistoryRepository extends JpaRepository<AiHistory, Long> {
    List<AiHistory> findBySessionIdOrderByCreatedAtAsc(Long sessionId);
}
