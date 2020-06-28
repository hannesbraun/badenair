package de.hso.badenair.service.flight.search;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import de.hso.badenair.domain.flight.Flight;
import de.hso.badenair.domain.plane.PlaneTypeData;
import de.hso.badenair.service.flight.repository.FlightRepository;
import de.hso.badenair.util.time.DateFusioner;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class  FlightSearchService {
	private final int BOOKING_TIME_LIMIT = 45;
	private final FlightRepository flightRepository;

	public List<Flight> getFlights(int start, int destination, int passengers,
			OffsetDateTime date) {
		List<Flight> flights = (List<Flight>) flightRepository.findAll();

		return flights.stream()
				.filter(flight -> this.hasSameAirports(flight, start,
						destination))
				.filter(flight -> this.isAroundSameDay(
						DateFusioner.fusionStartDate(flight.getStartDate(),
								flight.getScheduledFlight().getStartTime(),
								null),
						date))
				.filter(flight -> this.hasFreeSeats(flight, passengers))
				.filter(this::isAfterBookingTimeLimit)
				.collect(Collectors.toList());
	}

	private boolean isAroundSameDay(OffsetDateTime date1,
			OffsetDateTime date2) {
		long duration = Math.abs(ChronoUnit.DAYS.between(date1.toLocalDate(), date2.toLocalDate()));
		return duration < 8;
	}

	private boolean hasFreeSeats(Flight flight, int seats) {
		int takenSeats = flight.getBookings().stream().reduce(0,
				(sum, booking) -> booking.getTravelers().size() + sum,
				Integer::sum);

		PlaneTypeData data = flight.getPlane().getTypeData();
		int totalNumberOfSeats = data.getNumberOfColumns()
				* data.getNumberOfRows();

		return (takenSeats + seats) <= totalNumberOfSeats;
	}

	private boolean hasSameAirports(Flight flight, int startingAirportId,
			int destinationAirportId) {
		long currentStartingId = flight.getScheduledFlight()
				.getStartingAirport().getId();
		long currentDestinationId = flight.getScheduledFlight()
				.getDestinationAirport().getId();

		return currentStartingId == startingAirportId
				&& currentDestinationId == destinationAirportId;
	}

	private boolean isAfterBookingTimeLimit(Flight flight) {
		OffsetDateTime startDate = DateFusioner.fusionStartDate(
				flight.getStartDate(),
				flight.getScheduledFlight().getStartTime(), null);

		return OffsetDateTime.now().withOffsetSameLocal(ZoneOffset.of("+1"))
				.isBefore(startDate.minusMinutes(this.BOOKING_TIME_LIMIT));
	}
}
