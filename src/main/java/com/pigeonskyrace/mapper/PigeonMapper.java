package com.pigeonskyrace.mapper;

import com.pigeonskyrace.dto.reponse.PigeonResponseDTO;
import com.pigeonskyrace.dto.request.PigeonRequestDTO;
import com.pigeonskyrace.model.Colombier;
import com.pigeonskyrace.model.Pigeon;
import com.pigeonskyrace.service.ColombierService;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public interface PigeonMapper {

    PigeonMapper INSTANCE = Mappers.getMapper(PigeonMapper.class);


    @Mapping(source = "numeroBague", target = "numeroBague")
    Pigeon toPigeon(PigeonRequestDTO pigeonRequestDTO);

    PigeonResponseDTO toPigeonResponseDTO(Pigeon pigeon);


    default String map(ObjectId value) {
        return value != null ? value.toHexString() : null;
    }

    default ObjectId map(String value) {
        return value != null ? new ObjectId(value) : null;
    }
}