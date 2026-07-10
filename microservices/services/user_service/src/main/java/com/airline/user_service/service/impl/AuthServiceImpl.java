package com.airline.user_service.service.impl;

import com.airline.enums.UserRole;
import com.airline.payload.dto.UserDTO;
import com.airline.payload.response.AuthResponse;
import com.airline.user_service.config.JwtProvider;
import com.airline.user_service.mapper.UserMapper;
import com.airline.user_service.model.User;
import com.airline.user_service.repository.UserRepository;
import com.airline.user_service.service.AuthService;
import com.airline.user_service.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;
    /*
        1. Check if email already exists
        2. Encode password using BCrypt
        3. Save user in databse
        4. Generate JWT token
        5. Return token and user information
     */

    @Override
    public AuthResponse login(String email, String password) throws Exception {
         Authentication authentication = authenticate(email,password);

         User user = userRepository.findByEmail(email);
         user.setLastLogin(LocalDateTime.now());
         userRepository.save(user);
         String jwt = jwtProvider.generateToken(authentication, user.getId());
         AuthResponse authResponse = new AuthResponse();
         authResponse.setJwt(jwt);
         authResponse.setUserDTO(UserMapper.toDTO(user));
         authResponse.setTitle("Welcome "+user.getFullName());
         authResponse.setMessage("Logged in successfully");
         return authResponse;
    }

    private Authentication authenticate(String email,String password) throws Exception {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        if(!passwordEncoder.matches(
                password,userDetails.getPassword()
        )){
            throw new Exception("Invalid password");
        }
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }

     /*
        1. Load user by email
        2. Compare password with BCrypt
        3. update 'lastLogin' time
        4. Generate JWT token
        5. Return token and user information
     */

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
                .userRole(req.getRole())
                .lastLogin(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        User savedUser = userRepository.save(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                savedUser.getEmail(),savedUser.getPassword()
        );

        String jwt = jwtProvider.generateToken(authentication, savedUser.getId());
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setUserDTO(UserMapper.toDTO(savedUser));
        authResponse.setTitle("Welcome "+savedUser.getFullName());
        authResponse.setMessage("Registered successfully");

        return authResponse;
    }
}
