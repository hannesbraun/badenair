package de.hso.badenair.service.flight.booking;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import de.hso.badenair.controller.dto.flight.IncomingBookingDto;
import de.hso.badenair.controller.dto.seat.SelectedSeatDto;
import de.hso.badenair.controller.dto.traveler.IncomingTravelerDto;
import de.hso.badenair.controller.flight.PriceCalculator;
import de.hso.badenair.domain.booking.Booking;
import de.hso.badenair.domain.booking.Luggage;
import de.hso.badenair.domain.booking.LuggageState;
import de.hso.badenair.domain.booking.Traveler;
import de.hso.badenair.domain.flight.Flight;
import de.hso.badenair.service.booking.repository.BookingRepository;
import de.hso.badenair.service.flight.FlightService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService {
	private final BookingRepository bookingRepository;
	private final FlightService flightService;

	private final List<Integer> allowedWeights = List.of(15, 23, 30);

	public boolean bookFlight(String username, IncomingBookingDto dto) {
		Flight flight = flightService.getFlightById(dto.getFlightId());
		if (flight != null) {
			Set<Traveler> travelers = new HashSet<>();
			Booking newBooking = Booking.builder().flight(flight)
					.customerUserId(username).price(dto.getPrice()).build();

			for (int i = 0; i < dto.getPassengers().length; i++) {
				IncomingTravelerDto travelerDto = dto.getPassengers()[i];
				SelectedSeatDto selectedSeat = dto.getSeats()[i];
				Traveler traveler = Traveler.builder()
						.firstName(travelerDto.getName())
						.lastName(travelerDto.getSurname())
						.checkedIn(travelerDto.isCheckedIn())
						.booking(newBooking)
						.seatNumber("" + selectedSeat.getRow()
								+ Character.toString(
										(char) (65 + selectedSeat.getColumn())))
						.build();
				travelers.add(traveler);

				Set<Luggage> luggages = new HashSet<>();
				if (allowedWeights.contains(travelerDto.getBaggage1())) {
					Luggage luggage1 = Luggage.builder()
							.state(LuggageState.AT_TRAVELLER)
							.weight(travelerDto.getBaggage1())
							.traveler(traveler).build();
					luggages.add(luggage1);
				}

				if (allowedWeights.contains(travelerDto.getBaggage2())) {
					Luggage luggage2 = Luggage.builder()
							.state(LuggageState.AT_TRAVELLER)
							.weight(travelerDto.getBaggage2())
							.traveler(traveler).build();
					luggages.add(luggage2);
				}
				if (allowedWeights.contains(travelerDto.getBaggage3())) {
					Luggage luggage3 = Luggage.builder()
							.state(LuggageState.AT_TRAVELLER)
							.weight(travelerDto.getBaggage3())
							.traveler(traveler).build();
					luggages.add(luggage3);
				}
				if (allowedWeights.contains(travelerDto.getBaggage4())) {
					Luggage luggage4 = Luggage.builder()
							.state(LuggageState.AT_TRAVELLER)
							.weight(travelerDto.getBaggage4())
							.traveler(traveler).build();
					luggages.add(luggage4);
				}
				if (!luggages.isEmpty()) {
					traveler.setLuggage(luggages);
				}
			}
			if (!travelers.isEmpty()) {
				newBooking.setTravelers(travelers);
				PriceCalculator.calculateFinalPrice(newBooking);
				bookingRepository.save(newBooking);
				return true;
			}
		}

		return false;
	}
}
