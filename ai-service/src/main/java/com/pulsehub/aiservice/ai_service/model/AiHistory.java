package com.pulsehub.aiservice.ai_service.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing the chat history of a session.
 * Stores both user question and AI response.
 */
@Entity
@Table(name = "ai_history")
public class AiHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(columnDefinition = "TEXT")
    private String question;

    @Column(columnDefinition = "TEXT")
    private String response;

    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "session_id")
    private AiChatSession session;

    public AiHistory() {}

    public AiHistory(Long userId, String question, String response, AiChatSession session) {
        this.userId = userId;
        this.question = question;
        this.response = response;
        this.session = session;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public AiChatSession getSession() { return session; }
    public void setSession(AiChatSession session) { this.session = session; }
}
