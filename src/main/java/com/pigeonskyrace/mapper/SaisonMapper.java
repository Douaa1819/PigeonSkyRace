package com.pigeonskyrace.mapper;

import com.pigeonskyrace.dto.reponse.SaisonReponseDTO;
import com.pigeonskyrace.dto.reponse.CompetionReponseDTO;  // Import CompetionReponseDTO
import com.pigeonskyrace.dto.request.SaisonRequestDTO;
import com.pigeonskyrace.model.Saison;
import com.pigeonskyrace.model.Competion;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
@Mapper(componentModel = "spring", uses = {CompetionMapper.class})
public interface SaisonMapper {

    SaisonMapper INSTANCE = Mappers.getMapper(SaisonMapper.class);

    @Mappings({
            @Mapping(source = "id", target = "id", qualifiedByName = "competionMapperObjectIdToString"),
            @Mapping(source = "date", target = "date"),
            @Mapping(source = "nom", target = "nom"),
            @Mapping(source = "description", target = "description"),
            @Mapping(source = "competions", target = "competions")
    })
    SaisonReponseDTO toDto(Saison saison);

    @Mappings({
            @Mapping(source = "date", target = "date"),
            @Mapping(source = "nom", target = "nom"),
            @Mapping(source = "description", target = "description"),
            @Mapping(target = "competions", ignore = true)
    })
    Saison toEntity(SaisonRequestDTO saisonRequestDTO);


    @Named("objectIdToString")
    default String objectIdToString(ObjectId objectId) {
        return objectId != null ? objectId.toHexString() : null;
    }


    @Named("competionMapperObjectIdToString")
    default String competionMapperObjectIdToString(ObjectId objectId) {
        return objectId != null ? objectId.toHexString() : null;
    }


    @Named("stringToObjectId")
    default ObjectId stringToObjectId(String id) {
        return id != null ? new ObjectId(id) : null;
    }
}

