package de.hso.badenair.service.boardingpass;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import de.hso.badenair.domain.booking.Luggage;
import de.hso.badenair.domain.booking.Traveler;
import de.hso.badenair.service.luggage.LuggageRepository;
import de.hso.badenair.service.traveler.TravelerRepository;
import de.hso.badenair.util.time.DateFusioner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Service to generate boarding pass pdf documents
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BoardingPassService {

    private final TravelerRepository travelerRepository;

    private final LuggageRepository luggageRepository;

    /**
     * Retrieves the required data from the database and generates a boarding pass pdf using this data.
     * @param id the id of the traveler for which to generate the boarding pass
     * @return the generated boarding pass pdf document
     */
    public byte[] getBoardingPass(Long id) {
        Optional<Traveler> traveler = travelerRepository.findById(id);
        if (!traveler.isPresent()) {
            // Traveler not found
            return null;
        } else if (!traveler.get().isCheckedIn()) {
            // Traveler not checked in
            return null;
        }

        // Get luggage information
        List<Luggage> luggage = luggageRepository
            .findAllByTravelerId(traveler.get().getId());

        try {
            return generatePdf(traveler.get(), luggage);
        } catch (IOException e) {
            return null;
        }
    }

    // The following code may look ugly. Sorry.

    /**
     * Generates a boarding pass pdf document using the given data.
     * @param traveler the traveler data (this object should include references to the flight data as well)
     * @param luggage the list of luggage which the traveler has booked
     * @return the generated boarding pass pdf document
     * @throws IOException if writing to the pdf document fails (thrown by the pdfbox library)
     */
    private byte[] generatePdf(Traveler traveler, List<Luggage> luggage)
        throws IOException {
        // Duration
        Double durationInHours = traveler.getBooking().getFlight()
            .getScheduledFlight().getDurationInHours();
        int minutes = (int) ((durationInHours - durationInHours.intValue())
            * 60.0);
        String duration = durationInHours.intValue() + " Stunden, " + minutes
            + " Minuten";

        // Start date
        OffsetDateTime startDate = DateFusioner.fusionStartDate(
            traveler.getBooking().getFlight().getStartDate(),
            traveler.getBooking().getFlight().getScheduledFlight()
                .getStartTime(),
            traveler.getBooking().getFlight().getScheduledFlight()
                .getStartingAirport().getTimezone());
        String startDateString = startDate
            .format(DateTimeFormatter.ofPattern("dd. MMMM yyyy, HH:mm"))
            + " Uhr (" + startDate.getOffset()
            .getDisplayName(TextStyle.SHORT, Locale.GERMAN)
            + ")";

        // Arrival date
        OffsetDateTime arrivalDate = DateFusioner.fusionArrivalDate(
            traveler.getBooking().getFlight().getStartDate(),
            traveler.getBooking().getFlight().getScheduledFlight()
                .getStartTime(),
            durationInHours,
            traveler.getBooking().getFlight().getScheduledFlight()
                .getStartingAirport().getTimezone());
        String arrivalDateString = arrivalDate
            .format(DateTimeFormatter.ofPattern("dd. MMMM yyyy, HH:mm"))
            + " Uhr (" + arrivalDate.getOffset()
            .getDisplayName(TextStyle.SHORT, Locale.GERMAN)
            + ")";

        // QR code string
        StringBuilder qrCodeStringBuilder = new StringBuilder();
        qrCodeStringBuilder.append("BadenAir Bordkarte für ");
        qrCodeStringBuilder.append(traveler.getFirstName());
        qrCodeStringBuilder.append(" ");
        qrCodeStringBuilder.append(traveler.getLastName());
        qrCodeStringBuilder.append("\n--------\n\nFlugnummer: ");
        qrCodeStringBuilder.append(traveler.getBooking().getFlight()
            .getScheduledFlight().getId().toString());
        qrCodeStringBuilder.append("\nVon: ");
        qrCodeStringBuilder.append(traveler.getBooking().getFlight()
            .getScheduledFlight().getStartingAirport().getName());
        qrCodeStringBuilder.append("\nNach: ");
        qrCodeStringBuilder.append(traveler.getBooking().getFlight()
            .getScheduledFlight().getDestinationAirport().getName());
        qrCodeStringBuilder.append("\nFlugdauer: ");
        qrCodeStringBuilder.append(duration);
        qrCodeStringBuilder.append("\nStart: ");
        qrCodeStringBuilder.append(startDateString);
        qrCodeStringBuilder.append("\nLandung: ");
        qrCodeStringBuilder.append(arrivalDateString);
        qrCodeStringBuilder.append("\nGepäckstück 1: ");
        qrCodeStringBuilder.append(getLuggageString(luggage, 0));
        qrCodeStringBuilder.append("\nGepäckstück 2: ");
        qrCodeStringBuilder.append(getLuggageString(luggage, 1));
        qrCodeStringBuilder.append("\nGepäckstück 3: ");
        qrCodeStringBuilder.append(getLuggageString(luggage, 2));
        qrCodeStringBuilder.append("\nGepäckstück 4: ");
        qrCodeStringBuilder.append(getLuggageString(luggage, 3));

        // Document setup
        PDDocument document = new PDDocument();
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        PDPage page = new PDPage();
        document.addPage(page);
        PDPageContentStream contents = new PDPageContentStream(document, page);

        // Border for site
        float border = 39.0f;

        // Y length of boarding pass
        float yLength = 300.0f;

        // Size of QR code
        float qrCodeSize = 125.0f;

        // QR Code for whatever reason with the same information
        try {
            PDImageXObject qrCode = PDImageXObject.createFromByteArray(document,
                generateQRCode(qrCodeStringBuilder.toString()), null);
            contents.drawImage(qrCode, 612.0f - border - qrCodeSize,
                792.0f - border - yLength, qrCodeSize, qrCodeSize);
        } catch (WriterException | IOException e) {
            // Whatever... don't add the QR code
            e.printStackTrace();
        }

        // Add the BadenAir logo
        FileInputStream logoInputStream = new FileInputStream(
            ResourceUtils.getFile("classpath:logo2.png"));
        PDImageXObject logo = PDImageXObject.createFromByteArray(document,
            IOUtils.toByteArray(logoInputStream), "logo2.png");
        logoInputStream.close();
        float logoOffset = 5.0f;
        float logoSizeX = 585.0f * 0.14f;
        float logoSizeY = 375.0f * 0.14f;
        contents.drawImage(logo, border + logoOffset,
            792.0f - border - logoSizeY - logoOffset, logoSizeX, logoSizeY);

        // Demo ad
        FileInputStream adInputStream = new FileInputStream(
            ResourceUtils.getFile("classpath:ad_scaled.png"));
        PDImageXObject ad = PDImageXObject.createFromByteArray(document,
            IOUtils.toByteArray(adInputStream), "ad_scaled.png");
        adInputStream.close();
        float adSizeX = 1000.0f * 0.534f;
        float adSizeY = 705.0f * 0.534f;
        contents.drawImage(ad, border, border, adSizeX, adSizeY);

        // Draw rectangular border
        contents.setStrokingColor(Color.BLACK);
        contents.setLineWidth(2.5f);
        contents.moveTo(border, 792.0f - border);
        contents.lineTo(border, 792.0f - border - yLength);
        contents.lineTo(612.0f - border, 792.0f - border - yLength);
        contents.lineTo(612.0f - border, 792.0f - border);
        contents.lineTo(border, 792.0f - border);
        contents.closeAndStroke();

        // Draw other borders inside rectangle
        contents.setLineWidth(1.0f);

        // First horizontal line
        contents.moveTo(border, 792.0f - border - logoSizeY - logoOffset);
        contents.lineTo(612.0f - border,
            792.0f - border - logoSizeY - logoOffset);

        // Vertical line in header
        contents.moveTo(612.0f - border - logoSizeY - logoOffset,
            792.0f - border - logoSizeY - logoOffset);
        contents.lineTo(612.0f - border - logoSizeY - logoOffset,
            792.0f - border);

        // Second horizontal line
        float secondLineOffset = 117.0f;
        contents.moveTo(border,
            792.0f - border - logoSizeY - logoOffset - secondLineOffset);
        contents.lineTo(612.0f - border,
            792.0f - border - logoSizeY - logoOffset - secondLineOffset);

        contents.closeAndStroke();

        // Fonts to use
        PDFont fontBold = PDType1Font.HELVETICA_BOLD;
        PDFont fontNormal = PDType1Font.HELVETICA;

        // Header settings
        contents.setLeading(20.0f);

        // Passenger
        contents.beginText();
        contents.newLineAtOffset(border + logoSizeX + 14.0f,
            792.0f - border - 22.0f);
        contents.setNonStrokingColor(Color.DARK_GRAY);
        contents.setFont(fontBold, 11);
        contents.showText("Passagier");
        contents.newLine();
        contents.setNonStrokingColor(Color.BLACK);
        contents.setFont(fontNormal, 14);
        contents.showText(
            traveler.getFirstName() + " " + traveler.getLastName());
        contents.endText();

        // Seat number
        String[] seatLetters = {"A", "B", "C", "D", "E", "F"};
        contents.beginText();
        contents.newLineAtOffset(612.0f - border - logoSizeY + 1.0f,
            792.0f - border - 22.0f);
        contents.setNonStrokingColor(Color.DARK_GRAY);
        contents.setFont(fontBold, 11);
        contents.showText("Sitzplatz");
        contents.newLine();
        contents.setNonStrokingColor(Color.BLACK);
        contents.setFont(fontNormal, 14);
        contents.showText(traveler.getSeatRow() + "" + seatLetters[traveler.getSeatColumn()]);
        contents.endText();

        // Body settings
        contents.setLeading(17.0f);

        // Start/destination
        contents.beginText();
        contents.newLineAtOffset(border + 14.0f,
            792.0f - border - logoSizeY - 24.0f);
        contents.setNonStrokingColor(Color.DARK_GRAY);
        contents.setFont(fontBold, 10);
        contents.showText("Flugnummer");
        contents.newLine();
        contents.setNonStrokingColor(Color.BLACK);
        contents.setFont(fontNormal, 13);
        contents.showText(traveler.getBooking().getFlight().getScheduledFlight()
            .getId().toString());
        contents.newLine();
        contents.setNonStrokingColor(Color.DARK_GRAY);
        contents.setFont(fontBold, 10);
        contents.showText("Von");
        contents.newLine();
        contents.setNonStrokingColor(Color.BLACK);
        contents.setFont(fontNormal, 13);
        contents.showText(traveler.getBooking().getFlight().getScheduledFlight()
            .getStartingAirport().getName());
        contents.newLine();
        contents.setNonStrokingColor(Color.DARK_GRAY);
        contents.setFont(fontBold, 10);
        contents.showText("Nach");
        contents.newLine();
        contents.setNonStrokingColor(Color.BLACK);
        contents.setFont(fontNormal, 13);
        contents.showText(traveler.getBooking().getFlight().getScheduledFlight()
            .getDestinationAirport().getName());
        contents.endText();

        // Time stuff
        contents.beginText();
        contents.newLineAtOffset(612.0f / 2.0f + 14.0f,
            792.0f - border - logoSizeY - 24.0f);
        contents.setNonStrokingColor(Color.DARK_GRAY);
        contents.setFont(fontBold, 10);
        contents.showText("Flugdauer");
        contents.newLine();
        contents.setNonStrokingColor(Color.BLACK);
        contents.setFont(fontNormal, 13);
        contents.showText(duration);
        contents.newLine();
        contents.setNonStrokingColor(Color.DARK_GRAY);
        contents.setFont(fontBold, 10);
        contents.showText("Start");
        contents.newLine();
        contents.setNonStrokingColor(Color.BLACK);
        contents.setFont(fontNormal, 13);
        contents.showText(startDateString);
        contents.newLine();
        contents.setNonStrokingColor(Color.DARK_GRAY);
        contents.setFont(fontBold, 10);
        contents.showText("Landung");
        contents.newLine();
        contents.setNonStrokingColor(Color.BLACK);
        contents.setFont(fontNormal, 13);
        contents.showText(arrivalDateString);
        contents.endText();

        // Luggage
        contents.beginText();
        contents.newLineAtOffset(border + 14.0f, 792.0f - border - logoSizeY
            - logoOffset - secondLineOffset - 34.0f);
        contents.setNonStrokingColor(Color.DARK_GRAY);
        contents.setFont(fontBold, 10);
        contents.showText("Gepäckstück 1");
        contents.newLine();
        contents.setNonStrokingColor(Color.BLACK);
        contents.setFont(fontNormal, 13);
        contents.showText(getLuggageString(luggage, 0));
        contents.setLeading(30.0f);
        contents.newLine();
        contents.setNonStrokingColor(Color.DARK_GRAY);
        contents.setFont(fontBold, 10);
        contents.showText("Gepäckstück 2");
        contents.setLeading(17.0f);
        contents.newLine();
        contents.setNonStrokingColor(Color.BLACK);
        contents.setFont(fontNormal, 13);
        contents.showText(getLuggageString(luggage, 1));
        contents.endText();
        contents.beginText();
        contents.newLineAtOffset((612.0f - qrCodeSize) / 2.0f + 14.0f, 792.0f
            - border - logoSizeY - logoOffset - secondLineOffset - 34.0f);
        contents.setNonStrokingColor(Color.DARK_GRAY);
        contents.setFont(fontBold, 10);
        contents.showText("Gepäckstück 3");
        contents.newLine();
        contents.setNonStrokingColor(Color.BLACK);
        contents.setFont(fontNormal, 13);
        contents.showText(getLuggageString(luggage, 2));
        contents.setLeading(30.0f);
        contents.newLine();
        contents.setNonStrokingColor(Color.DARK_GRAY);
        contents.setFont(fontBold, 10);
        contents.showText("Gepäckstück 4");
        contents.setLeading(17.0f);
        contents.newLine();
        contents.setNonStrokingColor(Color.BLACK);
        contents.setFont(fontNormal, 13);
        contents.showText(getLuggageString(luggage, 3));
        contents.endText();

        // End of document content
        contents.close();

        // Save document
        document.save(result);
        document.close();

        return result.toByteArray();
    }

    /**
     * Generates a qr code using the given string.
     * @param input the string that the qr code should contain
     * @return the generated qr code picture (format: png)
     * @throws WriterException if contents can't be encoded legally in a format
     * @throws IOException if writing to the byte array stream fails
     */
    private byte[] generateQRCode(String input)
        throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(input, BarcodeFormat.QR_CODE,
            1024, 1024);

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", result);
        return result.toByteArray();
    }

    /**
     * Returns the string to display in the boarding pass for a given piece of luggage.
     * @param list the list of booked luggage (can be smaller than the requested index)
     * @param index the index of the piece of luggage of which to get the string for
     * @return the constructed luggage string
     */
    private String getLuggageString(List<Luggage> list, int index) {
        if (list.size() <= index) {
            return "Nicht gebucht";
        } else {
            return "Gebucht (maximal " + list.get(index).getWeight() + " kg)";
        }
    }
}
