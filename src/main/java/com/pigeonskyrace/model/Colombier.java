package com.pigeonskyrace.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "colombiers")
public class Colombier {

    @MongoId
    private ObjectId id;

    @Indexed(unique = true)
    @NotBlank(message = "Le nom du colombier est requis")

    @Size(max = 50, message = "Le nom du colombier ne doit pas dépasser 50 caractères")
    private String nomColombier;

    @Size(max=90 ,min=-90)
    private double coordonneeGPSlatitude;

    @Size(max=180 ,min=-180)
    private double coordonneeGPSlongitude;

    // Référence vers l'utilisateur qui possède ce colombier
    @DBRef
    private User proprietaire;

    // Liste des pigeons associés à ce colombier
    @DBRef
    private List<Pigeon> pigeons;

    public void setProprietaireId(ObjectId userId) {
        User user = new User();
        user.setId(userId);
        this.proprietaire = user;
    }

}