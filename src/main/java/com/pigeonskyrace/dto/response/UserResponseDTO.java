package com.pigeonskyrace.dto.response;

import com.pigeonskyrace.model.enums.Role;
import lombok.Data;

@Data
public class UserResponseDTO {
    private String id;
    private String name;
    private String email;
    private Role role;
}
