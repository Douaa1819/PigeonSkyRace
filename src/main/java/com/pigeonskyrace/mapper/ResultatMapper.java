package com.pigeonskyrace.mapper;

import com.pigeonskyrace.dto.reponse.ResultatReponseDTO;
import com.pigeonskyrace.dto.request.ResultatRequestDTO;
import com.pigeonskyrace.model.Resultat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ResultatMapper {

    @Mapping(source = "dateArrivee", target = "dateArrivee")
    Resultat toEntity(ResultatRequestDTO resultatRequestDTO);
    @Mapping(target = "id", expression = "java(resultat.getId().toHexString())")
    ResultatReponseDTO toReponseDTO(Resultat resultat);


}
