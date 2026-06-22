package com.classmate.authservice.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private final JwtService jwtService = new JwtService();

    @Test
    void shouldGenerateAndExtractEmail() {

        String email = "test@classmate.com";

        String token = jwtService.generateToken(email);

        assertNotNull(token);
        assertFalse(token.isBlank());

        String extractedEmail =
                jwtService.extractEmail(token);

        assertEquals(email, extractedEmail);
    }
}