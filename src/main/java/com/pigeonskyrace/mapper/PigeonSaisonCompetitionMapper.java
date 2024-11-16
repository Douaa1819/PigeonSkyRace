package com.pigeonskyrace.mapper;

import com.pigeonskyrace.dto.request.PigeonSaisonCompetitionRequestDTO;
import com.pigeonskyrace.dto.reponse.PigeonSaisonCompetitionResponseDTO;
import com.pigeonskyrace.model.PigeonSaisonCompetition;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface PigeonSaisonCompetitionMapper {

    @Mappings({
            @Mapping(source = "saisonPigeonId", target = "saisonPigeonId", qualifiedByName = "stringToObjectId"),
            @Mapping(source = "competitionId", target = "competitionId", qualifiedByName = "stringToObjectId"),
            @Mapping(target = "id", ignore = true)
    })
    PigeonSaisonCompetition toEntity(PigeonSaisonCompetitionRequestDTO requestDTO);

    @Mappings({
            @Mapping(source = "id", target = "id", qualifiedByName = "objectIdToString"),
            @Mapping(source = "saisonPigeonId", target = "saisonPigeonId", qualifiedByName = "objectIdToString"),
            @Mapping(source = "competitionId", target = "competitionId", qualifiedByName = "objectIdToString")
    })
    PigeonSaisonCompetitionResponseDTO toResponseDTO(PigeonSaisonCompetition entity);

    // Méthode pour convertir un ObjectId en String
    @Named("objectIdToString")  // Ajoutez l'annotation @Named
    default String objectIdToString(ObjectId objectId) {
        return objectId != null ? objectId.toHexString() : null;
    }

    // Méthode pour convertir une String en ObjectId
    @Named("stringToObjectId")  // Ajoutez l'annotation @Named
    default ObjectId stringToObjectId(String id) {
        return id != null && !id.isEmpty() ? new ObjectId(id) : null;
    }
}
