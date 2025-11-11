package com.pulsehub.aiservice.ai_service.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing an AI chat session.
 * Each user can have multiple sessions, each containing multiple messages.
 */
@Entity
@Table(name = "ai_chat_sessions")
public class AiChatSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String title; // short preview or user-given name of the session

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // -----------------------------------------------------
    // Constructors
    // -----------------------------------------------------
    public AiChatSession() {
        this.createdAt = LocalDateTime.now();
    }

    public AiChatSession(Long userId, String title) {
        this.userId = userId;
        this.title = title;
        this.createdAt = LocalDateTime.now();
    }

    /** Constructor for referencing existing sessions (for saving history) */
    public AiChatSession(Long id) {
        this.id = id;
    }

    // -----------------------------------------------------
    // Getters and Setters
    // -----------------------------------------------------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // -----------------------------------------------------
    // Utility
    // -----------------------------------------------------
    @Override
    public String toString() {
        return "AiChatSession{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}

