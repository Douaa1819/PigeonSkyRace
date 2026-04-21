package com.pigeonskyrace.mapper;

import com.pigeonskyrace.dto.response.ResultatResponseDTO;
import com.pigeonskyrace.dto.request.ResultatRequestDTO;
import com.pigeonskyrace.model.Resultat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ResultatMapper {

    @Mapping(source = "dateArrivee", target = "dateArrivee")
    Resultat toEntity(ResultatRequestDTO resultatRequestDTO);

    @Mapping(target = "id", expression = "java(resultat.getId() != null ? resultat.getId().toHexString() : null)")
    @Mapping(source = "classement", target = "rank")
    @Mapping(target = "pigeonId", ignore = true)
    @Mapping(target = "ringNumber", ignore = true)
    @Mapping(target = "loftName", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    ResultatResponseDTO toResponseDTO(Resultat resultat);
}
