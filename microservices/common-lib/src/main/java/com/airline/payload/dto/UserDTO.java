package com.airline.payload.dto;

import com.airline.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private String email;
    private String password;
    private String fullName;
    private String phone;
    private UserRole role;
    private String username;
    private LocalDateTime lastLogin;

    public UserDTO(Long id, String email, String fullName,
                   UserRole role, LocalDateTime lastLogin) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.password = null;
        this.phone = null;
        this.username = null;
        this.lastLogin = lastLogin;
    }

}
