package com.pigeonskyrace.dto.response;

/**
 * Login / register response: JWT plus public user profile (no password fields).
 */
public record AuthResponseDTO(
        String accessToken,
        UserResponseDTO user
) {
}
