package com.pigeonskyrace.mapper;

import com.pigeonskyrace.dto.reponse.CompetionReponseDTO;
import com.pigeonskyrace.dto.request.CompetionRequestDTO;
import com.pigeonskyrace.model.Competion;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
@Mapper(componentModel = "spring")
public interface CompetionMapper {

    CompetionMapper INSTANCE = Mappers.getMapper(CompetionMapper.class);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "nom", target = "nom"),
            @Mapping(source = "latitudeGPS", target = "latitudeGPS"),
            @Mapping(source = "longitudeGPS", target = "longitudeGPS"),
            @Mapping(source = "nombrePigeons", target = "nbPigeons"),
            @Mapping(source = "pourcentageAdmission", target = "pourcentageAdmission"),
            @Mapping(source = "saison.id", target = "saisonId", qualifiedByName = "objectIdToString")
    })
    CompetionReponseDTO toDto(Competion competition);

    @Mappings({
            @Mapping(source = "nom", target = "nom"),
            @Mapping(source = "latitudeGPS", target = "latitudeGPS"),
            @Mapping(source = "longitudeGPS", target = "longitudeGPS"),
            @Mapping(source = "nbPigeons", target = "nombrePigeons"),
            @Mapping(target = "pourcentageAdmission", constant = "25.0"),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "saison", ignore = true)
    })
    Competion toEntity(CompetionRequestDTO competitionRequestDTO);



    // Map ObjectId to String
    @Named("objectIdToString")
    default String objectIdToString(ObjectId objectId) {
        return objectId != null ? objectId.toHexString() : null;
    }

    // Map String to ObjectId
    @Named("stringToObjectId")
    default ObjectId stringToObjectId(String id) {
        return id != null ? new ObjectId(id) : null;
    }
}