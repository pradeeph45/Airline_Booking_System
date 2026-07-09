package com.airline.user_service.service;

import com.airline.payload.dto.UserDTO;
import com.airline.payload.response.AuthResponse;

public interface AuthService {

    AuthResponse login(String email, String password);
    AuthResponse signUp(UserDTO req) throws Exception;
}
