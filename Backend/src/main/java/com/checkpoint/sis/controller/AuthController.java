package com.checkpoint.sis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.checkpoint.sis.dto.LoginRequest;
import com.checkpoint.sis.dto.LoginResponse;
import com.checkpoint.sis.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    // We will create this service in the next step
    @Autowired
    private AuthService authService;

    /**
     * Endpoint for all users (students, admins) to log in.
     * URL: POST /api/v1/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        // The authService will handle validation and token generation
        String token = authService.login(loginRequest);

        // Return the token in the response
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
