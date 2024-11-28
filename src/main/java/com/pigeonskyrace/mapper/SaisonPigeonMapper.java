package com.pigeonskyrace.mapper;
import com.pigeonskyrace.dto.reponse.SaisonPigeonResponseDTO;
import com.pigeonskyrace.dto.request.SaisonPigeonRequestDTO;
import com.pigeonskyrace.model.SaisonPigeon;
import org.bson.types.ObjectId;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SaisonPigeonMapper {

    SaisonPigeonMapper INSTANCE = Mappers.getMapper(SaisonPigeonMapper.class);

    SaisonPigeon toEntity(SaisonPigeonRequestDTO dto);
    @Mapping(target = "id",expression = "java(saisonPigeon.getId().toHexString())")
    @Mapping(target = "saison",expression = "java(saisonPigeon.getSaison().getId().toHexString())")
    @Mapping(target = "pigeon",expression = "java(saisonPigeon.getPigeon().getId().toHexString())")
    SaisonPigeonResponseDTO toDto(SaisonPigeon saisonPigeon);
}

