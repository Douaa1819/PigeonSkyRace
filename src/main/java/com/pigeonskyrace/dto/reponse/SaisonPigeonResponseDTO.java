package com.pigeonskyrace.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pigeonskyrace.model.Pigeon;
import com.pigeonskyrace.model.Saison;
import com.pigeonskyrace.model.SaisonPigeon;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class SaisonPigeonResponseDTO {

    private String id;


    private String saison;


    private String pigeon;


    public SaisonPigeon toEntity() {
        SaisonPigeon saisonPigeon = new SaisonPigeon();
        saisonPigeon.setId(new ObjectId(this.id));
       Saison saison1=new Saison();
        saison1.setId(new ObjectId(this.saison));

        Pigeon pigeon1=new Pigeon();
        pigeon1.setId(new ObjectId(this.pigeon));

        saisonPigeon.setPigeon(pigeon1);
        saisonPigeon.setSaison(saison1);
        return saisonPigeon;
    }
}
