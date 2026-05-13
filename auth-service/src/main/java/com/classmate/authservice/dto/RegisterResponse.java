package com.classmate.authservice.dto;

import com.classmate.authservice.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterResponse {

    private Long id;
    private String email;
    private Role role;
}