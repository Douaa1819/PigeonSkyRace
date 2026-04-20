package com.pigeonskyrace.service;

import com.pigeonskyrace.dto.request.LoginRequestDTO;
import com.pigeonskyrace.dto.request.RegisterRequestDTO;
import com.pigeonskyrace.dto.response.UserResponseDTO;
import com.pigeonskyrace.mapper.UserMapper;
import com.pigeonskyrace.model.User;
import com.pigeonskyrace.model.enums.Role;
import com.pigeonskyrace.repository.UserRepository;
import com.pigeonskyrace.security.JwtTokenProvider;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private UserService userService;

    @Test
    void registerCreatesBreederAndReturnsToken() {
        RegisterRequestDTO req = new RegisterRequestDTO();
        req.setName("Test");
        req.setEmail("a@b.com");
        req.setPassword("secret123");

        User user = new User();
        user.setId(new ObjectId());
        user.setEmail("a@b.com");
        user.setName("Test");
        user.setRole(Role.BREEDER);

        when(userRepository.findByEmail("a@b.com")).thenReturn(Optional.empty());
        when(userMapper.toEntity(any(RegisterRequestDTO.class))).thenReturn(user);
        when(passwordEncoder.encode("secret123")).thenReturn("hash");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(userMapper.toResponse(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            UserResponseDTO dto = new UserResponseDTO();
            dto.setId(u.getId().toHexString());
            dto.setName(u.getName());
            dto.setEmail(u.getEmail());
            dto.setRole(u.getRole());
            return dto;
        });
        when(jwtTokenProvider.createAccessToken(any(User.class))).thenReturn("jwt-token");

        var response = userService.register(req);

        assertNotNull(response);
        assertEquals("jwt-token", response.accessToken());
        assertEquals(Role.BREEDER, response.user().getRole());
    }

    @Test
    void loginReturnsTokenWhenPasswordMatches() {
        LoginRequestDTO req = new LoginRequestDTO();
        req.setEmail("a@b.com");
        req.setPassword("secret123");

        User user = new User();
        user.setId(new ObjectId());
        user.setEmail("a@b.com");
        user.setPassword("hash");
        user.setRole(Role.BREEDER);

        when(userRepository.findByEmail("a@b.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("secret123", "hash")).thenReturn(true);
        when(userMapper.toResponse(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            UserResponseDTO dto = new UserResponseDTO();
            dto.setId(u.getId().toHexString());
            dto.setEmail(u.getEmail());
            dto.setRole(u.getRole());
            return dto;
        });
        when(jwtTokenProvider.createAccessToken(user)).thenReturn("jwt");

        var response = userService.login(req);

        assertEquals("jwt", response.accessToken());
    }
}
