package com.classmate.authservice.controller;

import com.classmate.authservice.dto.LoginRequest;
import com.classmate.authservice.dto.LoginResponse;
import com.classmate.authservice.dto.RegisterRequest;
import com.classmate.authservice.dto.RegisterResponse;
import com.classmate.authservice.entity.Role;
import com.classmate.authservice.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {

        registerRequest = new RegisterRequest();

        registerRequest.setNombre("Patricio");
        registerRequest.setApellido("Espinoza");
        registerRequest.setEmail("pato@test.cl");
        registerRequest.setPassword("123456");
        registerRequest.setTelefono("999999999");
        registerRequest.setFechaNacimiento(
                LocalDate.of(1995, 1, 1)
        );

        loginRequest = new LoginRequest();

        loginRequest.setEmail("pato@test.cl");
        loginRequest.setPassword("123456");
    }

    @Test
    public void testRegister() throws Exception {

        RegisterResponse response =
                new RegisterResponse(
                        1L,
                        "pato@test.cl",
                        Role.ESTUDIANTE
                );

        when(authService.register(any(RegisterRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                registerRequest
                                        )
                                )
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email")
                        .value("pato@test.cl"))
                .andExpect(jsonPath("$.role")
                        .value("ESTUDIANTE"));
    }

    @Test
    public void testLogin() throws Exception {

        LoginResponse response =
                new LoginResponse("token-jwt");

        when(authService.login(any(LoginRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                loginRequest
                                        )
                                )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token")
                        .value("token-jwt"));
    }
}