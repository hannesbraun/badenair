package de.hso.badenair.controller.flight.search;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customer/public/flight")
@RequiredArgsConstructor
public class FlightSearchController {
	private final FlightSearchService flightService;

    @GetMapping("/search")
    public ResponseEntity<List<FlightDto>> getFlights(
        @RequestParam int start,
        @RequestParam int destination,
        @RequestParam int passengers,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime date) {

		final List<FlightDto> flights = this.flightService
				.getFlights(start, destination, passengers, date).stream()
				.map(flight -> {
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
				}).collect(Collectors.toList());

		return ResponseEntity.ok(flights);
	}
}
