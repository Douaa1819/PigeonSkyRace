package com.pigeonskyrace.service;

import com.pigeonskyrace.dto.reponse.CompetionReponseDTO;
import com.pigeonskyrace.dto.reponse.ResultatReponseDTO;
import com.pigeonskyrace.dto.reponse.SaisonPigeonResponseDTO;
import com.pigeonskyrace.dto.request.ResultatRequestDTO;
import com.pigeonskyrace.exception.EntityNotFoundException;
import com.pigeonskyrace.mapper.CompetionMapper;
import com.pigeonskyrace.mapper.ResultatMapper;
import com.pigeonskyrace.model.*;
import com.pigeonskyrace.repository.ResultatRepository;
import com.pigeonskyrace.utils.Coordinates;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Slf4j
@Service
@RequiredArgsConstructor
public class ResultatService {

    private final PigeonService pigeonService;
    private final SaisonPigeonService saisonPigeonService;
    private final PigeonSaisonCompetitionService pigeonSaisonCompetitionService;
    private final ColombierService colombierService;
    private final ResultatRepository resultatRepository;
    private final CompetionMapper competionMapper;
    private final ResultatMapper mapper;

    /**
     * Crée un résultat pour un pigeon dans une compétition et calcule sa vitesse.
     */
    public ResultatReponseDTO createResult(ResultatRequestDTO resultatRequestDTO, CompetionReponseDTO competition) throws ChangeSetPersister.NotFoundException {
        log.info("Début de la création du résultat pour le numéro de bague : {}", resultatRequestDTO.numeroBague());

        Pigeon pigeon = pigeonService.findByNumeroBague(resultatRequestDTO.numeroBague());
        if (pigeon == null) {
            log.error("Pigeon introuvable pour le numéro de bague : {}", resultatRequestDTO.numeroBague());
            throw new EntityNotFoundException("Pigeon introuvable pour le numéro de bague : " + resultatRequestDTO.numeroBague());
        }
        log.info("Pigeon trouvé : {}", pigeon);

        SaisonPigeonResponseDTO saisonPigeonResponseDTO = saisonPigeonService.getSaisonPigeonBySaisonIdAndPigeonId(
                competition.getSaisonId(), pigeon.getId().toHexString());
        log.info("SaisonPigeon récupéré : {}", saisonPigeonResponseDTO);

        PigeonSaisonCompetition pigeonSaisonCompetition = pigeonSaisonCompetitionService
                .findBySeasonPigeonAndCompetition(saisonPigeonResponseDTO.toEntity(), competionMapper.toEntityy(competition))
                .orElseThrow(() -> {
                    log.error("Aucune participation trouvée pour le pigeon {} dans la compétition {}",
                            pigeon.getId(), competition.getId());
                    return new EntityNotFoundException(
                            "Aucune participation trouvée pour le pigeon " + pigeon.getId() +
                                    " dans la compétition " + competition.getId());
                });
        log.info("Participation à la compétition récupérée : {}", pigeonSaisonCompetition);

        Resultat resultat = mapper.toEntity(resultatRequestDTO);
        resultat.setPigeonSaisonCompetition(pigeonSaisonCompetition);
        log.info("Résultat mappé : {}", resultat);


        Coordinates coordonneesColombier = colombierService.getLoftCoordinates(pigeon.getColombier().getId());
        log.info("Coordonnées du colombier : {}", coordonneesColombier);

        double distance = calculerDistance(
                coordonneesColombier.getLatitude(), coordonneesColombier.getLongitude(),
                competition.getLatitudeGPS(), competition.getLongitudeGPS());
        resultat.setDistance(distance);
        log.info("Distance calculée : {} km", distance);


        Duration tempsDeVol = Duration.between(competition.getStartTime(), resultat.getDateArrivee());
        log.info("Temps de vol calculé : {} heures", tempsDeVol.toHours());

        // Calculer la vitesse
        double vitesse = calculerVitesse(distance, tempsDeVol);
        resultat.setVitesse(vitesse);
        log.info("Vitesse calculée : {} km/h", vitesse);


        resultatRepository.save(resultat);
        log.info("Résultat sauvegardé avec succès : {}", resultat);

        // Retourner le résultat mappé en DTO
        ResultatReponseDTO resultatReponseDTO = mapper.toReponseDTO(resultat);
        log.info("Résultat retourné : {}", resultatReponseDTO);

        return resultatReponseDTO;
    }

    /**
     * Calcule le classement et les points pour tous les résultats d'une compétition.
     */
    public List<ResultatReponseDTO> calculatePoint(CompetionReponseDTO competitionDto) {
        // Utilisation de la méthode modifiée dans ResultatRepository
        List<PigeonSaisonCompetition> pigeonSaisonCompetitions = pigeonSaisonCompetitionService.findByCompetition(competionMapper.toEntityy(competitionDto));
        List<Resultat> resultats = new ArrayList<>();
        for(PigeonSaisonCompetition cp : pigeonSaisonCompetitions) {
            resultats.add(resultatRepository.findByPigeonSaisonCompetition(cp));
        }

        if (resultats.isEmpty()) {
            throw new EntityNotFoundException("Aucun résultat trouvé pour cette compétition.");
        }

        // Calcul de la distance moyenne pour ajuster les vitesses
        double distanceMoyenne = resultats.stream()
                .mapToDouble(Resultat::getDistance)
                .average()
                .orElse(0.0);

        // Appliquer le coefficient d'ajustement et recalculer les vitesses
        resultats.forEach(resultat -> {
            double coefficient = distanceMoyenne / resultat.getDistance();
            resultat.setVitesse(resultat.getVitesse() * coefficient);
        });

        // Trier par vitesse décroissante et attribuer classement/points
        resultats.sort((r1, r2) -> Double.compare(r2.getVitesse(), r1.getVitesse()));

        for (int i = 0; i < resultats.size(); i++) {
            Resultat resultat = resultats.get(i);
            resultat.setClassement(i + 1);
            resultat.setPoints(100.0 - (i * 100.0 / resultats.size())); // Exemple : 100 points max
            resultatRepository.save(resultat);
        }

        return resultats.stream()
                .map(mapper::toReponseDTO)
                .toList();
    }

    private double calculerVitesse(double distance, Duration tempsDeVol) {
        if (tempsDeVol.toMinutes() <= 0) {
            throw new IllegalArgumentException("Le temps de vol doit être supérieur à 0.");
        }
        return (distance * 1000) / tempsDeVol.toMinutes(); // Vitesse en m/min
    }


    private double calculerDistance(double lat1, double lon1, double lat2, double lon2) {
        // Haversine Formula
        double latRad1 = Math.toRadians(lat1);
        double lonRad1 = Math.toRadians(lon1);
        double latRad2 = Math.toRadians(lat2);
        double lonRad2 = Math.toRadians(lon2);

        double deltaLat = latRad2 - latRad1;
        double deltaLon = lonRad2 - lonRad1;

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(latRad1) * Math.cos(latRad2)
                * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        return 6371.01 * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
    public List<Resultat> getResultatsByCompetitionId(ObjectId competitionId) {
        List<PigeonSaisonCompetition> pigeonSaisonCompetitions = pigeonSaisonCompetitionService.findByCompetitionId(competitionId);
        List<Resultat> resultats = new ArrayList<>();
        for(PigeonSaisonCompetition cp : pigeonSaisonCompetitions) {
            resultats.add(resultatRepository.findByPigeonSaisonCompetition(cp));
        }
        return resultats;
    }
}