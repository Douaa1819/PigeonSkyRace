package com.pigeonskyrace.mapper;

import com.pigeonskyrace.dto.reponse.ResultatReponseDTO;
import com.pigeonskyrace.dto.request.ResultatRequestDTO;
import com.pigeonskyrace.model.Resultat;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResultatMapper {

Resultat toEntity(ResultatRequestDTO resultatRequestDTO);
ResultatReponseDTO toReponseDTO(Resultat resultat);

}
