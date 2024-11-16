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

    @Data
    public static class ColombierRequestDTO {

        @NotBlank(message = "Le nom du colombier est requis")
        @Size(max = 50, message = "Le nom du colombier ne doit pas dépasser 50 caractères")
        private String nomColombier;

        @Size(max = 100, message = "Les coordonnées GPS ne doivent pas dépasser 100 caractères")
        private String coordonneeGPS;
    }



        public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
           System.out.println("donneer moi une phrase");
           String mots= sc.nextLine();
        }



}