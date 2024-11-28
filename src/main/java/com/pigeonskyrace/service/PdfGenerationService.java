package com.pigeonskyrace.service;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.pigeonskyrace.dto.reponse.CompetionReponseDTO;
import com.pigeonskyrace.model.Resultat;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PdfGenerationService {

    public byte[] generateCompetitionResultPdf(CompetionReponseDTO competition, List<Resultat> resultats) throws DocumentException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();

        PdfWriter.getInstance(document, out);
        document.open();
        document.add(new Paragraph("Résultats de la compétition : " + competition.getNom(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
        document.add(new Paragraph("Date : " + competition.getStartTime() + " - " + competition.getEndTime()));
        document.add(new Paragraph("Lieu : " + competition.getLatitudeGPS() + ", " + competition.getLongitudeGPS()));

        document.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        table.addCell("Classement");
        table.addCell("Distance");
        table.addCell("Vitesse");
        table.addCell("Points");
        table.addCell("Date d'arrivée");

        for (Resultat resultat : resultats) {
            table.addCell(String.valueOf(resultat.getClassement()));
            table.addCell(String.format("%.2f km", resultat.getDistance()));
            table.addCell(String.format("%.2f km/h", resultat.getVitesse()));
            table.addCell(String.format("%.2f", resultat.getPoints()));
            table.addCell(resultat.getDateArrivee().toString());
        }

        document.add(table);
        document.close();
        return out.toByteArray();
    }
}
