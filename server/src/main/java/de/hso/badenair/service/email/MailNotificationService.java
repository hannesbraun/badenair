package de.hso.badenair.service.email;

import de.hso.badenair.domain.booking.Booking;
import de.hso.badenair.domain.flight.ScheduledFlight;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailNotificationService {

    private final MailService mailService;

    public void sendFlightScheduleChangeNotification(String recipient, String name, ScheduledFlight scheduledFlight, OffsetDateTime actualStartTime) throws IOException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        Map<String, String> values = Map.of(
            "name", name,
            "startingAirport", scheduledFlight.getStartingAirport().getName(),
            "destinationAirport", scheduledFlight.getDestinationAirport().getName(),
            "startTime", actualStartTime.format(dateTimeFormatter)
        );

        final String messageContent = TemplateProcessingUtil.processScheduleChangeTemplate(values);

        mailService.sendMail(recipient, "BadenAir Info", messageContent);
    }

    public void sendCancelledFlightNotification(String recipient, String name, ScheduledFlight scheduledFlight, OffsetDateTime actualStartTime) throws IOException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        Map<String, String> values = Map.of(
            "name", name,
            "startingAirport", scheduledFlight.getStartingAirport().getName(),
            "destinationAirport", scheduledFlight.getDestinationAirport().getName()
        );

        final String messageContent = TemplateProcessingUtil.processNoFlightTemplate(values);

        mailService.sendMail(recipient, "BadenAir Info", messageContent);
    }
    
    public void sendInvoiceNotification(String recipient, String name, List<Booking> bookings) throws IOException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        OffsetDateTime today = OffsetDateTime.now();

        final Long id = bookings.get(0).getId();
        final double total = bookings.stream().mapToDouble(Booking::getPrice).sum();

        Map<String, String> values = Map.of(
            "name", name,
            "total", String.valueOf(total),
            "dueDate", today.plusMonths(1).format(dateTimeFormatter),
            "invoiceId", "#" + id,
            "date", today.format(dateTimeFormatter)
        );

        final String messageContent = TemplateProcessingUtil.processInvoiceTemplate(values, bookings);

        mailService.sendMail(recipient, "BadenAir Rechnung", messageContent);
    }
}
