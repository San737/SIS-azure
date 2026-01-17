package com.checkpoint.sis.service;

import com.checkpoint.sis.dto.LoginRequest;

public interface AuthService {
    String login(LoginRequest loginRequest);
}
