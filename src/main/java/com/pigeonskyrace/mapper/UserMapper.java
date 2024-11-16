package com.pigeonskyrace.mapper;

import com.pigeonskyrace.dto.reponse.UserResponseDTO;
import com.pigeonskyrace.dto.request.RegisterRequestDTO;
import com.pigeonskyrace.model.User;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(RegisterRequestDTO request) {
        User user = new User();
        user.setId(new ObjectId());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());
        return user;
    }

    public UserResponseDTO toResponse(User user) {
        UserResponseDTO response = new UserResponseDTO();
        response.setId(user.getId().toHexString());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        return response;
    }
}
