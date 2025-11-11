package com.pulsehub.diaryservice.diary_service.service;

import com.pulsehub.diaryservice.diary_service.model.DiaryEntry;
import com.pulsehub.diaryservice.diary_service.repo.DiaryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DiaryService {

    private final DiaryRepository repo;

    public DiaryService(DiaryRepository repo) {
        this.repo = repo;
    }

    public List<DiaryEntry> getEntries(Long userId) {
        return repo.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public DiaryEntry addEntry(DiaryEntry entry) {
        entry.setCreatedAt(LocalDateTime.now());
        return repo.save(entry);
    }

    public Optional<DiaryEntry> updateEntry(Long id, DiaryEntry updated) {
        return repo.findById(id).map(entry -> {
            entry.setTitle(updated.getTitle());
            entry.setContent(updated.getContent());
            return repo.save(entry);
        });
    }

    public void deleteEntry(Long id) {
        repo.deleteById(id);
    }
}
