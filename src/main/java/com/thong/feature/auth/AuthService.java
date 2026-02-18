package com.thong.feature.auth;

import com.thong.feature.auth.dto.JwtResponse;
import com.thong.feature.auth.dto.LoginRequest;
import com.thong.feature.auth.dto.RefreshTokenRequest;
import com.thong.feature.auth.dto.RegisterRequest;

public interface AuthService {
    JwtResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
    JwtResponse login(LoginRequest loginRequest);
//    void register(RegisterRequest registerRequest);
}
