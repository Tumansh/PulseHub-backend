package com.pulsehub.aiservice.ai_service.service;

import com.pulsehub.aiservice.ai_service.model.AiChatSession;
import com.pulsehub.aiservice.ai_service.model.AiHistory;
import com.pulsehub.aiservice.ai_service.repo.AiChatSessionRepository;
import com.pulsehub.aiservice.ai_service.repo.AiHistoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class AiService {

    private final AiChatSessionRepository chatSessionRepository;
    private final AiHistoryRepository historyRepository;
    private final RestTemplate restTemplate;

    // ✅ Correct Groq-compatible endpoint
    @Value("${ai.api.url:https://api.groq.com/openai/v1/chat/completions}")
    private String aiApiUrl;

    @Value("${ai.api.key:YOUR_GROQ_API_KEY}")
    private String aiApiKey;

    public AiService(AiChatSessionRepository chatSessionRepository,
                     AiHistoryRepository historyRepository) {
        this.chatSessionRepository = chatSessionRepository;
        this.historyRepository = historyRepository;
        this.restTemplate = new RestTemplate();
    }

    // 🟢 Create chat session
    public AiChatSession createSession(Long userId, String title) {
        AiChatSession session = new AiChatSession(userId, title);
        return chatSessionRepository.save(session);
    }

    // 🟢 Get all user sessions
    public List<AiChatSession> getUserSessions(Long userId) {
        return chatSessionRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    // 🟢 Get chat history for a session
    public List<AiHistory> getSessionHistory(Long sessionId) {
        return historyRepository.findBySessionIdOrderByCreatedAtAsc(sessionId);
    }

    // 🟢 Save question-response pair
    public void save(Long userId, String question, String response, AiChatSession session) {
        AiHistory history = new AiHistory(userId, question, response, session);
        historyRepository.save(history);
    }

    // 🧠 Ask Groq / OpenAI API
    public String askExternal(String question, String context) {
        try {
            if (question == null || question.isBlank()) {
                return "Please provide a valid question.";
            }

            Map<String, Object> requestBody = Map.of(
                    "model", "llama-3.1-8b-instant",  // ✅ Updated Groq model
                    "messages", List.of(
                            Map.of("role", "system", "content", "You are an assistant for PulseHub."),
                            Map.of("role", "user", "content", question)
                    )
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(aiApiKey);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.exchange(aiApiUrl, HttpMethod.POST, entity, Map.class);

            Map<String, Object> data = response.getBody();
            if (data != null && data.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) data.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return message.get("content").toString();
                }
            }

            return "AI did not return a valid response.";

        } catch (Exception e) {
            System.err.println("AI API error: " + e.getMessage());
            return generateLocalFallbackResponse(question);
        }
    }

    // 🧩 Local fallback when Groq API unreachable
    private String generateLocalFallbackResponse(String question) {
        if (question == null) return "Sorry, I didn’t catch that.";
        String lower = question.toLowerCase();

        if (lower.contains("hello") || lower.contains("hi")) {
            return "👋 Hi there! PulseHub AI is online and ready to assist you.";
        } else if (lower.contains("expense")) {
            return "💰 You can track your expenses in the Expense tab.";
        } else if (lower.contains("weather")) {
            return "🌤️ Check your local weather on the dashboard.";
        } else if (lower.contains("stock")) {
            return "📈 Stock data is available under the Stocks section.";
        }

        return "🤖 (Offline mode) I couldn’t reach the AI API, but here’s a fallback answer: “" + question + "” sounds interesting!";
    }

    // 🟢 Get or create chat session
    public AiChatSession findOrCreateSessionByIdAndUser(Long sessionId, Long userId) {
        return chatSessionRepository.findById(sessionId)
                .filter(s -> s.getUserId().equals(userId))
                .orElseGet(() -> chatSessionRepository.save(new AiChatSession(userId, "New Session")));
    }

    // 🟢 Rename session
    @Transactional
    public void renameSession(Long userId, Long sessionId, String newTitle) {
        AiChatSession session = chatSessionRepository.findById(sessionId).orElse(null);
        if (session == null || !session.getUserId().equals(userId)) return;

        session.setTitle(newTitle);
        chatSessionRepository.saveAndFlush(session);

        System.out.println("Renamed session ID " + sessionId + " to " + newTitle);
    }

    // 🟢 Delete session
    @Transactional
    public void deleteSession(Long userId, Long sessionId) {
        chatSessionRepository.findById(sessionId).ifPresent(session -> {
            if (session.getUserId().equals(userId)) {
                historyRepository.deleteAll(
                        historyRepository.findBySessionIdOrderByCreatedAtAsc(sessionId)
                );
                chatSessionRepository.delete(session);
            }
        });
    }
}
