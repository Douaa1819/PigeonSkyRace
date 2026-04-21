package com.pigeonskyrace.service;

import com.pigeonskyrace.dto.response.AuthResponseDTO;
import com.pigeonskyrace.dto.response.UserResponseDTO;
import com.pigeonskyrace.dto.request.LoginRequestDTO;
import com.pigeonskyrace.dto.request.RegisterRequestDTO;
import com.pigeonskyrace.mapper.UserMapper;
import com.pigeonskyrace.model.User;
import com.pigeonskyrace.model.enums.Role;
import com.pigeonskyrace.repository.UserRepository;
import com.pigeonskyrace.security.JwtTokenProvider;
import com.pigeonskyrace.security.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(
            UserRepository userRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AuthResponseDTO register(RegisterRequestDTO request) {
        String normalizedEmail = normalizeEmail(request.getEmail());
        if (userRepository.findByEmail(normalizedEmail).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already in use");
        }
        User user = userMapper.toEntity(request);
        user.setRole(Role.BREEDER);
        user.setEmail(normalizedEmail);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return buildAuthResponse(user);
    }

    public AuthResponseDTO login(LoginRequestDTO request) {
        String normalizedEmail = normalizeEmail(request.getEmail());
        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        return buildAuthResponse(user);
    }

    public UserResponseDTO getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (!SecurityUtils.isAdmin() && !SecurityUtils.requireUserIdHex().equals(user.getId().toHexString())) {
            throw new AccessDeniedException("Cannot access this profile");
        }
        return userMapper.toResponse(user);
    }

    private AuthResponseDTO buildAuthResponse(User user) {
        String token = jwtTokenProvider.createAccessToken(user);
        UserResponseDTO userDto = userMapper.toResponse(user);
        return new AuthResponseDTO(token, userDto);
    }

    private String normalizeEmail(String email) {
        if (email == null) {
            return null;
        }
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
