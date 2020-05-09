package de.hso.badenair.service.flight.search;

import de.hso.badenair.domain.flight.Flight;
import de.hso.badenair.domain.plane.PlaneTypeData;
import de.hso.badenair.service.flight.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlightSearchService {
    private final int BOOKING_TIME_LIMIT = 45;
    private final FlightRepository flightRepository;

    public List<Flight> getFlights(int start, int destination, int passengers, Date date) {
        List<Flight> flights = (List<Flight>) flightRepository.findAll();

        OffsetDateTime selectedDate = date.toInstant()
            .atOffset(ZoneOffset.UTC);

        return flights.stream()
            .filter(flight -> this.hasSameAirports(flight, start, destination))
            .filter(flight -> this.isSameDay(flight.getScheduledFlight().getStartTime(), selectedDate))
            .filter(flight -> this.hasFreeSeats(flight, passengers))
            .filter(this::isAfterBookingTimeLimit)
            .collect(Collectors.toList());
    }

    private boolean isSameDay(OffsetDateTime date1, OffsetDateTime date2) {
        Instant instant1 = date1.toInstant()
            .truncatedTo(ChronoUnit.DAYS);
        Instant instant2 = date2.toInstant()
            .truncatedTo(ChronoUnit.DAYS);
        return instant1.equals(instant2);
    }

    private boolean hasFreeSeats(Flight flight, int seats) {
        int takenSeats = flight.getBookings().stream()
            .reduce(0, (sum, booking) -> booking.getTravelers().size() + sum, Integer::sum);

        PlaneTypeData data = flight.getPlane().getTypeData();
        int totalNumberOfSeats = data.getNumberOfColumns() * data.getNumberOfRows();

        return (takenSeats + seats) <= totalNumberOfSeats;
    }

    private boolean hasSameAirports(Flight flight, int startingAirportId, int destinationAirportId) {
        long currentStartingId = flight.getScheduledFlight().getStartingAirport().getId();
        long currentDestinationId = flight.getScheduledFlight().getDestinationAirport().getId();

        return currentStartingId == startingAirportId && currentDestinationId == destinationAirportId;
    }

    private boolean isAfterBookingTimeLimit(Flight flight) {
        OffsetDateTime startTime = flight.getActualStartTime();

        return OffsetDateTime.now().isBefore(startTime.minusMinutes(this.BOOKING_TIME_LIMIT));
    }
}
