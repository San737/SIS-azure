package com.checkpoint.sis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.checkpoint.sis.dto.LoginRequest;
import com.checkpoint.sis.service.AuthService;
import com.checkpoint.sis.util.JwtUtil;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public String login(LoginRequest loginRequest) {
        // Step 1: Authenticate the user using Spring Security's manager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        // If authentication is successful, the user's details are in the authentication object
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Step 2: Generate the JWT token for this user
        String token = jwtUtil.generateToken(userDetails);

        return token;
    }
}
