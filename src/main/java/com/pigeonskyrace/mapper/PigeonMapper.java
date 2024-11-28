package com.pigeonskyrace.mapper;

import com.pigeonskyrace.dto.reponse.PigeonResponseDTO;
import com.pigeonskyrace.dto.request.PigeonRequestDTO;
import com.pigeonskyrace.model.Pigeon;
import com.pigeonskyrace.model.enums.Sexe;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.Random;

@Mapper(componentModel = "spring")
public interface PigeonMapper {

    PigeonMapper INSTANCE = Mappers.getMapper(PigeonMapper.class);

    @Mapping(source = "colombierId", target = "colombier.id")
    @Mapping(source = "numeroBague", target = "numeroBague")
    Pigeon toPigeon(PigeonRequestDTO pigeonRequestDTO);

    @Mapping(source = "colombier.id", target = "colombierId")
    PigeonResponseDTO toPigeonResponseDTO(Pigeon pigeon);

    // Méthode pour générer le numéro de bague si absent
    default String generateNumeroBague(Sexe sexe, Integer age) {
        if (sexe == null || age == null) {
            return null; // Return null if sexe or age is null
        }
        String prefix = (sexe == Sexe.FEMALE) ? "F" : "M";
        Random rand = new Random();
        int n = rand.nextInt(90) + 10; // Génère un nombre à deux chiffres

        // Calcul de l'année de naissance
        int birthYear = LocalDate.now().minusYears(age).getYear();
        String yearSuffix = String.valueOf(birthYear).substring(2); // Prend les deux derniers chiffres de l'année

        return prefix + "**" + n + "-" + yearSuffix;
    }

    // Méthode par défaut pour assurer la génération du numeroBague si nécessaire
    default String mapNumeroBague(PigeonRequestDTO pigeonRequestDTO) {
        return pigeonRequestDTO.getNumeroBague() != null ? pigeonRequestDTO.getNumeroBague() : generateNumeroBague(pigeonRequestDTO.getSexe(), pigeonRequestDTO.getAge());
    }

    default String map(ObjectId value) {
        return value != null ? value.toHexString() : null;
    }

    default ObjectId map(String value) {
        return value != null ? new ObjectId(value) : null;
    }
}
