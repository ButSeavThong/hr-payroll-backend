package com.thong.feature.auth;

import com.thong.feature.auth.dto.JwtResponse;
import com.thong.feature.auth.dto.LoginRequest;
import com.thong.feature.auth.dto.RefreshTokenRequest;
import com.thong.feature.auth.dto.RegisterRequest;
import com.thong.feature.user.UserSerivce;
import com.thong.feature.user.dto.CreateUserRequest;
import com.thong.feature.user.dto.UserProfileResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserSerivce userSerivce;
    private final AuthService authService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/refresh")
    @PreAuthorize("permitAll()")
    public JwtResponse refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    UserProfileResponse register(@Valid @RequestBody CreateUserRequest registerRequest){
       return userSerivce.register(registerRequest);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    JwtResponse login(@Valid @RequestBody LoginRequest loginRequest){
        return  authService.login(loginRequest);
    }

    @GetMapping("/profile/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public UserProfileResponse getProfile(@PathVariable Integer id) {
        return userSerivce.getUserProfile(id);
    }
}
