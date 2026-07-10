package com.airline.user_service.controller;

import com.airline.payload.dto.UserDTO;
import com.airline.payload.request.LoginRequest;
import com.airline.payload.response.AuthResponse;
import com.airline.user_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signUp(@RequestBody @Valid UserDTO userDTO) throws Exception {
        AuthResponse response = authService.signUp(userDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> signUp(@RequestBody @Valid LoginRequest request) throws Exception {
        AuthResponse response = authService.login(request.getEmail(),request.getPassword());
        return ResponseEntity.ok(response);
    }
}
