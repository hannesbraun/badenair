package de.hso.badenair.util.mapper;

import de.hso.badenair.controller.dto.flight.FlightDto;
import de.hso.badenair.controller.flight.PriceCalculator;
import de.hso.badenair.domain.booking.Booking;
import de.hso.badenair.domain.flight.Flight;
import de.hso.badenair.util.time.DateFusioner;

import java.time.OffsetDateTime;

public abstract class FlightMapper {

    public static FlightDto mapToDto(Flight flight) {
        int takenSeats = 0;
        for (Booking booking : flight.getBookings()) {
            takenSeats += booking.getTravelers().size();
        }

        // Dates
        OffsetDateTime startDate = DateFusioner
            .fusionStartDate(flight.getStartDate(),
                flight.getScheduledFlight().getStartTime(),
                flight.getScheduledFlight()
                    .getStartingAirport()
                    .getTimezone());
        OffsetDateTime arrivalDate = DateFusioner.fusionArrivalDate(
            flight.getStartDate(),
            flight.getScheduledFlight().getStartTime(),
            flight.getScheduledFlight().getDurationInHours(),
            flight.getScheduledFlight().getStartingAirport()
                .getTimezone());

        return new FlightDto(flight.getId(),
            flight.getScheduledFlight().getStartingAirport()
                .getName(),
            flight.getScheduledFlight().getDestinationAirport()
                .getName(),
            startDate, arrivalDate,
            PriceCalculator.calcFlightPriceRaw(
                flight.getScheduledFlight().getBasePrice(),
                takenSeats,
                flight.getPlane().getTypeData()
                    .getNumberOfPassengers(),
                startDate));
    }
}