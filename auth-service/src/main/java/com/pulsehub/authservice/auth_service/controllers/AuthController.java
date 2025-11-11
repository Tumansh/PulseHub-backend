package com.pulsehub.authservice.auth_service.controllers;

import com.pulsehub.authservice.auth_service.model.User;
import com.pulsehub.authservice.auth_service.repo.UserRepository;
import com.pulsehub.commonlib.common_lib.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("{\"message\":\"Email already exists\"}");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail(), user.getId());
        return ResponseEntity.ok(new AuthResponse(token, user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginUser) {
        Optional<User> optionalUser = userRepository.findByEmail(loginUser.getEmail());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(401).body("{\"message\":\"Invalid credentials\"}");
        }

        User user = optionalUser.get();
        if (!passwordEncoder.matches(loginUser.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("{\"message\":\"Invalid credentials\"}");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getId());
        return ResponseEntity.ok(new AuthResponse(token, user));
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestHeader("Authorization") String authHeader,
                                            @RequestBody PasswordChangeRequest request) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("{\"message\":\"Missing or invalid token\"}");
            }

            String token = authHeader.substring(7);
            String email = jwtUtil.extractEmail(token);

            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(404).body("{\"message\":\"User not found\"}");
            }

            User user = optionalUser.get();
            if (!passwordEncoder.matches(request.oldPassword, user.getPassword())) {
                return ResponseEntity.status(400).body("{\"message\":\"Old password is incorrect\"}");
            }

            user.setPassword(passwordEncoder.encode(request.newPassword));
            userRepository.save(user);

            return ResponseEntity.ok("{\"success\":true,\"message\":\"Password changed successfully\"}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("{\"message\":\"Something went wrong\"}");
        }
    }

    public static class PasswordChangeRequest {
        public String oldPassword;
        public String newPassword;
    }

    public static class AuthResponse {
        public String token;
        public User user;
        public AuthResponse(String token, User user) {
            this.token = token;
            this.user = user;
        }
    }
}
