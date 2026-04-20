package com.pigeonskyrace.controller;

import com.pigeonskyrace.dto.response.AuthResponseDTO;
import com.pigeonskyrace.dto.response.UserResponseDTO;
import com.pigeonskyrace.dto.request.LoginRequestDTO;
import com.pigeonskyrace.dto.request.RegisterRequestDTO;
import com.pigeonskyrace.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody @Valid RegisterRequestDTO registerRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequest) {
        return ResponseEntity.ok(userService.login(loginRequest));
    }

    /**
     * Stateless JWT: client discards the token. This endpoint exists for symmetry with the SPA.
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable String id) {
        String cleanedId = id.trim();
        return ResponseEntity.ok(userService.getUserById(cleanedId));
    }
}
