package com.thong.feature.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateProfileRequest(
        String username,
        String email,
        @JsonProperty("profileImage")
        String profileImage
) {
}
