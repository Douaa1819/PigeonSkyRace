package com.pigeonskyrace.mapper;

import com.pigeonskyrace.dto.reponse.ColombierReponseDTO;
import com.pigeonskyrace.dto.reponse.PigeonResponseDTO;
import com.pigeonskyrace.dto.request.ColombierRequestDTO;
import com.pigeonskyrace.model.Colombier;
import com.pigeonskyrace.model.Pigeon;
import org.bson.types.ObjectId;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ColombierMapper {


    @Named("objectIdToString")
    default String objectIdToString(ObjectId objectId) {
        return objectId != null ? objectId.toHexString() : null;
    }

    @Named("stringToObjectId")
    default ObjectId stringToObjectId(String id) {
        return id != null ? new ObjectId(id) : null;
    }
    @Mapping(source = "id", target = "id", qualifiedByName = "objectIdToString")
    PigeonResponseDTO toPigeonResponseDTO(Pigeon pigeon);

    @Named("toPigeonResponseDTOList")
    default List<PigeonResponseDTO> toPigeonResponseDTOList(List<Pigeon> pigeons) {
        return pigeons.stream()
                .map(this::toPigeonResponseDTO)
                .collect(Collectors.toList());
    }

    Colombier toColombier(ColombierRequestDTO colombierRequestDTO, @Context ObjectId userId);

    @Mapping(source = "user.id", target = "userId", qualifiedByName = "objectIdToString")
    @Mapping(source = "id", target = "id", qualifiedByName = "objectIdToString")
    @Mapping(source = "pigeons", target = "pigeons", qualifiedByName = "toPigeonResponseDTOList")
    ColombierReponseDTO toColombierResponseDTO(Colombier colombier);
}