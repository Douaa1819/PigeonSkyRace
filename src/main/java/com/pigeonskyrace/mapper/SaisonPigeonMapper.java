package com.pigeonskyrace.mapper;
import com.pigeonskyrace.dto.reponse.SaisonPigeonResponseDTO;
import com.pigeonskyrace.dto.request.SaisonPigeonRequestDTO;
import com.pigeonskyrace.model.SaisonPigeon;
import org.bson.types.ObjectId;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
public class SaisonPigeonMapper {

    public SaisonPigeon toEntity(SaisonPigeonRequestDTO dto, String saisonId) {
        SaisonPigeon saisonPigeon = new SaisonPigeon();
        saisonPigeon.setSaisonId(new ObjectId(saisonId));
        saisonPigeon.setPigeonId(dto.getPigeonId());
        return saisonPigeon;
    }


    public SaisonPigeonResponseDTO toDto(SaisonPigeon saisonPigeon) {
        SaisonPigeonResponseDTO responseDTO = new SaisonPigeonResponseDTO();
        responseDTO.setId(saisonPigeon.getId());
        responseDTO.setSaisonId(saisonPigeon.getSaisonId());
        responseDTO.setPigeonId(saisonPigeon.getPigeonId());
        return responseDTO;
    }


}

