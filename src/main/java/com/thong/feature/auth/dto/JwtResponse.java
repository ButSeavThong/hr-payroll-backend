package com.thong.feature.auth.dto;

import lombok.Builder;

@Builder
public record JwtResponse(
        String tokenType, // Bearer
        String accessToken,
        String refreshToken
) {
}