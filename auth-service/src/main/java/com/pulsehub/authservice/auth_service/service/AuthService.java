package com.pulsehub.authservice.auth_service.service;

import com.pulsehub.authservice.auth_service.model.User;
import com.pulsehub.authservice.auth_service.repo.UserRepository;
import com.pulsehub.authservice.auth_service.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse signup(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        // include userId in token
        String token = jwtUtil.generateToken(user.getEmail(), user.getId());
        return new AuthResponse(token, user);
    }

    public AuthResponse login(User loginUser) {
        Optional<User> optionalUser = userRepository.findByEmail(loginUser.getEmail());
        if (!optionalUser.isPresent()) {
            throw new RuntimeException("Invalid credentials");
        }

        User user = optionalUser.get();
        if (!passwordEncoder.matches(loginUser.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // include userId in token
        String token = jwtUtil.generateToken(user.getEmail(), user.getId());
        return new AuthResponse(token, user);
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
