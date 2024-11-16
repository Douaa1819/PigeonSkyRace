package com.pigeonskyrace.mapper;

import com.pigeonskyrace.dto.reponse.ColombierReponseDTO;
import com.pigeonskyrace.dto.reponse.UserResponseDTO;
import com.pigeonskyrace.dto.request.ColombierRequestDTO;
import com.pigeonskyrace.model.Colombier;
import org.bson.types.ObjectId;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface ColombierMapper {

    ColombierMapper INSTANCE = Mappers.getMapper(ColombierMapper.class);

    @Named("objectIdToString")
    default String objectIdToString(ObjectId objectId) {
        return objectId != null ? objectId.toHexString() : null;
    }


    @Mapping(target = "proprietaire.id", source = "userId")
    Colombier toColombier(ColombierRequestDTO colombierRequestDTO, @Context ObjectId userId);

    @Mapping(source = "proprietaire.id", target = "proprietaireId", qualifiedByName = "objectIdToString")
    ColombierReponseDTO toColombierResponseDTO(Colombier colombier);
}