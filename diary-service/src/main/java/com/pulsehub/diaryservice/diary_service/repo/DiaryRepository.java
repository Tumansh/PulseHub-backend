package com.pulsehub.diaryservice.diary_service.repo;

import com.pulsehub.diaryservice.diary_service.model.DiaryEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DiaryRepository extends JpaRepository<DiaryEntry, Long> {
    List<DiaryEntry> findByUserIdOrderByCreatedAtDesc(Long userId);
}
