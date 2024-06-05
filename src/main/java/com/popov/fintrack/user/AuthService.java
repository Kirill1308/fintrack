package com.popov.fintrack.user;

import com.popov.fintrack.user.dto.auth.JwtRequest;
import com.popov.fintrack.user.dto.auth.JwtResponse;


public interface AuthService {
    JwtResponse login(
            JwtRequest loginRequest
    );

    JwtResponse refresh(
            String refreshToken
    );
}
