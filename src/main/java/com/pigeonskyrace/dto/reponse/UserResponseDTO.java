package com.pigeonskyrace.dto.reponse;


import com.pigeonskyrace.model.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Scanner;

@Data
public class UserResponseDTO {
    private String id;
    private String name;
    private String email;
    private Role role;



}