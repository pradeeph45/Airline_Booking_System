package com.airline.user_service.service.impl;

import com.airline.enums.UserRole;
import com.airline.payload.dto.UserDTO;
import com.airline.payload.response.AuthResponse;
import com.airline.user_service.model.User;
import com.airline.user_service.repository.UserRepository;
import com.airline.user_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    /*
        1. Check if email already exists
        2. Encode password using BCrypt
        3. Save user in databse
        4. Generate JWT token
        5. Return token and user information
     */

    @Override
    public AuthResponse login(String email, String password) {

        return null;
    }

    @Override
    public AuthResponse signUp(UserDTO req) throws Exception {
        User existingUser = userRepository.findByEmail(req.getEmail());
        if(existingUser!=null){
            throw new Exception("Email already registered");
        }

        if(req.getRole() == UserRole.ROLE_SYSTEM_ADMIN){
            throw new Exception("You cannot sign up system admin!");
        }

        User user = User.builder()
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .fullName(req.getFullName())
                .phone(req.getPhone())
                .lastLogin(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        User savedUser = userRepository.save(user);
        return null;
    }
}
