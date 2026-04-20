package com.pigeonskyrace.dto.response;

public record AuthResponseDTO(
        String accessToken,
        String tokenType,
        long expiresInMs,
        UserResponseDTO user
) {
}
