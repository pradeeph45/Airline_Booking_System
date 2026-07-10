package com.airline.user_service.service;

import com.airline.payload.dto.UserDTO;
import com.airline.user_service.model.User;

import java.util.List;

public interface UserService {

    UserDTO getUserByEmail(String email) throws Exception;
    UserDTO getUserById(Long id) throws Exception;
    List<UserDTO> getAllUsers();
}
