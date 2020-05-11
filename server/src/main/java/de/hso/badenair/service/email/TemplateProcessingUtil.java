package de.hso.badenair.service.email;

import de.hso.badenair.domain.booking.Booking;
import de.hso.badenair.domain.flight.ScheduledFlight;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class TemplateProcessingUtil {

    private static final String INVOICE_DETAILS_KEY = "invoiceDetails";

    public static String processScheduleChangeTemplate(Map<String, String> values) throws IOException {
        String messageContent = getFileAsString("mail/scheduleChange.html");

        for (Map.Entry<String, String> entry : values.entrySet()) {
            messageContent = messageContent.replaceAll(getTemplateKey(entry.getKey()), entry.getValue());
        }

        return messageContent;
    }

    public static String processInvoiceTemplate(Map<String, String> values, List<Booking> bookings) throws IOException {
        String messageContent = getFileAsString("mail/invoice.html");

        for (Map.Entry<String, String> entry : values.entrySet()) {
            messageContent = messageContent.replaceAll(getTemplateKey(entry.getKey()), entry.getValue());
        }

        final String invoiceDetailTemplate = getFileAsString("mail/invoice_detail_template.html");
        final List<String> invoiceDetails = new ArrayList<>();


        bookings.forEach(booking -> {
            final ScheduledFlight scheduledFlight = booking.getFlight().getScheduledFlight();
            final Map<String, String> invoiceDetailsValues = Map.of(
                "startingAirport", scheduledFlight.getStartingAirport().getName(),
                "destinationAirport", scheduledFlight.getDestinationAirport().getName(),
                "numberOfPeople", String.valueOf(booking.getTravelers().size()),
                "amount", String.valueOf(booking.getPrice())
            );

            String content = invoiceDetailTemplate;

            for (Map.Entry<String, String> entry : invoiceDetailsValues.entrySet()) {
                content = content.replaceAll(getTemplateKey(entry.getKey()), entry.getValue());
            }

            invoiceDetails.add(content);
        });

        messageContent = messageContent.replaceAll(getTemplateKey(INVOICE_DETAILS_KEY), String.join("\n", invoiceDetails));

        return messageContent;
    }

    private static String getFileAsString(String path) throws IOException {
        final File file = new ClassPathResource(path).getFile();
        final byte[] bytes = FileCopyUtils.copyToByteArray(file);
        return new String(bytes);
    }

    private static String getTemplateKey(String keyname) {
        return "\\{\\{" + keyname + "\\}\\}";
    }
}
