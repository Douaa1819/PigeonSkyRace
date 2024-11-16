package com.pigeonskyrace.controller;

import com.pigeonskyrace.dto.reponse.UserResponseDTO;
import com.pigeonskyrace.dto.request.LoginRequestDTO;
import com.pigeonskyrace.dto.request.RegisterRequestDTO;
import com.pigeonskyrace.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@SessionAttributes("user")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequestDTO registerRequest) {
        try {
            String responseMessage = userService.register(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequest, HttpSession session) {
        try {
            String responseMessage = userService.login(loginRequest);

            // Récupérez l'ID de l'utilisateur connecté et stockez-le dans la session
            ObjectId userId = userService.findUserIdByEmail(loginRequest.getEmail());
            session.setAttribute("userId", userId);

            return ResponseEntity.ok(responseMessage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate(); // Détruit la session
        return ResponseEntity.ok("User logged out successfully.");
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable String id, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        try {
            String cleanedId = id.trim();
            UserResponseDTO userResponse = userService.getUserById(cleanedId);
            return ResponseEntity.ok(userResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}