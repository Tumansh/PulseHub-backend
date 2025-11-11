package com.pulsehub.aiservice.ai_service.controller;

import com.pulsehub.aiservice.ai_service.model.AiChatSession;
import com.pulsehub.aiservice.ai_service.model.AiHistory;
import com.pulsehub.aiservice.ai_service.service.AiService;
import com.pulsehub.commonlib.common_lib.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "http://localhost:5173")
public class AiController {

    private final AiService aiService;
    private final JwtUtil jwtUtil;

    public AiController(AiService aiService, JwtUtil jwtUtil) {
        this.aiService = aiService;
        this.jwtUtil = jwtUtil;
    }

    private Long getUserId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;
        String token = authHeader.substring(7);
        return jwtUtil.extractUserId(token);
    }

    // 🟢 Create a new chat session
    @PostMapping("/session")
    public ResponseEntity<AiChatSession> createSession(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody Map<String, String> body) {
        Long userId = getUserId(authHeader);
        if (userId == null) return ResponseEntity.status(401).build();

        String title = body.getOrDefault("title", "New Chat");
        AiChatSession session = aiService.createSession(userId, title);
        return ResponseEntity.ok(session);
    }

    // 🟢 Get user sessions
    @GetMapping("/sessions")
    public ResponseEntity<List<AiChatSession>> getSessions(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId = getUserId(authHeader);
        if (userId == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(aiService.getUserSessions(userId));
    }

    // 🟢 Rename chat session
    @PatchMapping("/session/{sessionId}")
    public ResponseEntity<?> renameSession(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Long sessionId,
            @RequestBody Map<String, String> body) {

        Long userId = getUserId(authHeader);
        if (userId == null) return ResponseEntity.status(401).build();

        String newTitle = body.get("title");
        aiService.renameSession(userId, sessionId, newTitle);
        return ResponseEntity.ok(Map.of("message", "Session renamed successfully"));
    }

    // 🟢 Delete chat session
    @DeleteMapping("/session/{sessionId}")
    public ResponseEntity<?> deleteSession(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Long sessionId) {
        Long userId = getUserId(authHeader);
        if (userId == null) return ResponseEntity.status(401).build();
        aiService.deleteSession(userId, sessionId);
        return ResponseEntity.ok(Map.of("message", "Session deleted"));
    }

    // 🟢 Get chat history
    @GetMapping("/history/{sessionId}")
    public ResponseEntity<List<AiHistory>> getSessionHistory(@PathVariable Long sessionId) {
        return ResponseEntity.ok(aiService.getSessionHistory(sessionId));
    }

    // 🟢 Ask question (with existing session)
    @PostMapping("/ask/{sessionId}")
    public ResponseEntity<?> askWithSession(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Long sessionId,
            @RequestBody Map<String, String> body) {
        Long userId = getUserId(authHeader);
        String question = body.get("question");
        String context = body.getOrDefault("context", "");
        String aiResponse = aiService.askExternal(question, context);

        if (userId != null) {
            AiChatSession session = aiService.findOrCreateSessionByIdAndUser(sessionId, userId);
            aiService.save(userId, question, aiResponse, session);
        }

        return ResponseEntity.ok(Map.of("response", aiResponse));
    }

    // 🟢 Ask question (guest / no session)
    @PostMapping("/ask")
    public ResponseEntity<?> askNoSession(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody Map<String, String> body) {
        Long userId = getUserId(authHeader);
        String question = body.get("question");
        String context = body.getOrDefault("context", "");
        String aiResponse = aiService.askExternal(question, context);

        if (userId != null) {
            AiChatSession session = aiService.createSession(userId, question.length() > 25 ? question.substring(0, 25) + "..." : question);
            aiService.save(userId, question, aiResponse, session);
            return ResponseEntity.ok(Map.of("response", aiResponse, "session", session));
        }

        return ResponseEntity.ok(Map.of("response", aiResponse));
    }
}
