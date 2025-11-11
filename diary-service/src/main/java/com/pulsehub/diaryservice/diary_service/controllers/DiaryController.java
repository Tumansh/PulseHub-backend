package com.pulsehub.diaryservice.diary_service.controllers;

import com.pulsehub.commonlib.common_lib.security.JwtUtil;
import com.pulsehub.diaryservice.diary_service.model.DiaryEntry;
import com.pulsehub.diaryservice.diary_service.repo.DiaryRepository;
import com.pulsehub.diaryservice.diary_service.service.DiaryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/diary")
@CrossOrigin(origins = "http://localhost:5173")
public class DiaryController {

    private final DiaryService diaryService;
    private final DiaryRepository diaryRepository;
    private final JwtUtil jwtUtil;

    public DiaryController(DiaryService diaryService, DiaryRepository diaryRepository, JwtUtil jwtUtil) {
        this.diaryService = diaryService;
        this.diaryRepository = diaryRepository;
        this.jwtUtil = jwtUtil;
    }

    private Long getUserIdFromAuthHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;
        String token = authHeader.substring(7);
        return jwtUtil.extractUserId(token);
    }

    @GetMapping
    public ResponseEntity<List<DiaryEntry>> getEntries(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        Long userId = getUserIdFromAuthHeader(authHeader);
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        List<DiaryEntry> entries = diaryService.getEntries(userId);
        return ResponseEntity.ok(entries);
    }

    @PostMapping
    public ResponseEntity<DiaryEntry> createEntry(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody DiaryEntry body) {

        Long userId = getUserIdFromAuthHeader(authHeader);
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        DiaryEntry entry = new DiaryEntry();
        entry.setUserId(userId);
        entry.setTitle(body.getTitle());
        entry.setContent(body.getContent());
        entry.setCreatedAt(LocalDateTime.now());
        DiaryEntry saved = diaryService.addEntry(entry);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiaryEntry> updateEntry(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Long id,
            @RequestBody DiaryEntry body) {

        Long userId = getUserIdFromAuthHeader(authHeader);
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Optional<DiaryEntry> existing = diaryRepository.findById(id);
        if (existing.isEmpty()) return ResponseEntity.notFound().build();

        DiaryEntry e = existing.get();
        if (!userId.equals(e.getUserId())) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        e.setTitle(body.getTitle());
        e.setContent(body.getContent());
        DiaryEntry saved = diaryRepository.save(e);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntry(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Long id) {

        Long userId = getUserIdFromAuthHeader(authHeader);
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Optional<DiaryEntry> opt = diaryRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        DiaryEntry e = opt.get();
        if (!userId.equals(e.getUserId())) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        diaryService.deleteEntry(id);
        return ResponseEntity.noContent().build();
    }
}
