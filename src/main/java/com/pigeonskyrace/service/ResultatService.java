package com.pigeonskyrace.service;

import com.pigeonskyrace.dto.reponse.CompetionReponseDTO;
import com.pigeonskyrace.dto.reponse.ResultatReponseDTO;
import com.pigeonskyrace.dto.request.ResultatRequestDTO;
import com.pigeonskyrace.exception.EntityNotFoundException;
import com.pigeonskyrace.mapper.ResultatMapper;
import com.pigeonskyrace.model.*;
import com.pigeonskyrace.repository.ResultatRepository;
import com.pigeonskyrace.utils.Coordinates;
import com.pigeonskyrace.utils.ObjectIdUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class ResultatService {

    private final PigeonService pigeonService;
    private final SaisonPigeonService saisonPigeonService;
    private final PigeonSaisonCompetitionService pigeonSaisonCompetitionService;
    private final ColombierService colombierService;
    private final ResultatRepository resultatRepository;
    private final ResultatMapper mapper;

    public ResultatReponseDTO createResult(ResultatRequestDTO resultatRequestDTO, CompetionReponseDTO competition) throws ChangeSetPersister.NotFoundException {
        Pigeon pigeon = pigeonService.findByNumeroBague(resultatRequestDTO.numeroBague());


        // Utiliser la méthode utilitaire pour convertir le String en ObjectId
        SaisonPigeon saisonPigeon = saisonPigeonService.getSaisonPigeonBySaisonIdAndPigeonId(
                ObjectIdUtil.stringToObjectId(competition.getSaisonId()), pigeon.id());

        PigeonSaisonCompetition pigeonSaisonCompetition = pigeonSaisonCompetitionService
                .findBySeasonPigeonAndCompetition(competition.getId(), saisonPigeon.getId());

        if (pigeonSaisonCompetition == null) {
            throw new EntityNotFoundException(
                    "Aucune participation trouvée pour le pigeon " + saisonPigeon.getId() +
                            " dans la compétition " + competition.getId());
        }


        Resultat resultat = mapper.toEntity(resultatRequestDTO);
        resultat.setPigeonSaisonCompetition(pigeonSaisonCompetition);

        // Calculer la distance
        Coordinates coordonneesColombier = colombierService.getLoftCoordinates(pigeon.id());
        double distance = calculerDistance(
                coordonneesColombier.getLatitude(), coordonneesColombier.getLongitude(),
                competition.getLatitudeGPS(), competition.getLongitudeGPS());

        resultat.setDistance(distance);

        // Calculer le temps de vol
        Duration tempsDeVol = Duration.between(competition.getStartTime(), resultat.getDateArrivee());
        resultat.setVitesse(calculerVitesse(distance, tempsDeVol));

        // Sauvegarder le résultat
        resultatRepository.save(resultat);

        return mapper.toReponseDTO(resultat);
    }

    private Double calculerVitesse(Double distance, Duration tempsDeVol) {
        if (tempsDeVol.toMinutes() <= 0) {
            throw new IllegalArgumentException("Le temps de vol doit être supérieur à 0.");
        }
        return (distance * 1000) / tempsDeVol.toMinutes(); // Convertir la distance en mètres
    }

    public static double calculerDistance(double lat1, double lon1, double lat2, double lon2) {
        double loftLatRad = Math.toRadians(lat1);
        double loftLonRad = Math.toRadians(lon1);
        double competitionLatRad = Math.toRadians(lat2);
        double competitionLonRad = Math.toRadians(lon2);

        double deltaLat = competitionLatRad - loftLatRad;
        double deltaLon = competitionLonRad - loftLonRad;

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(loftLatRad) * Math.cos(competitionLatRad)
                * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return 6371.01 * c;
    }
}
