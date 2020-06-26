package de.hso.badenair.service.flight.booking;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import de.hso.badenair.service.email.MailNotificationService;
import de.hso.badenair.service.keycloakapi.KeycloakApiService;
import de.hso.badenair.service.keycloakapi.dto.UserRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import de.hso.badenair.service.seat.SeatService;
import de.hso.badenair.util.time.DateFusioner;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService {
	private final BookingRepository bookingRepository;
	private final FlightService flightService;
	private final SeatService seatService;
	private final KeycloakApiService keycloakApiService;
	private final MailNotificationService mailNotificationService;

	private final List<Integer> allowedWeights = List.of(15, 23, 30);

	private static String currentUser = null;
	private static final Map<Integer, Booking> bookingDrafts = new HashMap<>();
	private static OffsetDateTime lastBookingDraft;
	private static final long TIMEOUT = 5l;
	private static final long BOOKING_LIMIT_MINUTES = 45l;

	private static final Random random = new Random();

	@Transactional
	public int bookFlight(String username, IncomingBookingDto dto) {
		synchronized (bookingDrafts) {
			if (currentUser != null && !currentUser.equals(username)) {
				// Currently locked by another user

				if (!lastBookingDraft.isBefore(OffsetDateTime.now().minusSeconds(TIMEOUT))) {
					return 0;
				} else {
					// Timeout, delete drafts
					unlock();
				}
			}

			Flight flight = flightService.getFlightById(dto.getFlightId());
			if (flight == null) {
				unlock();
				return 0;
			}

			if (DateFusioner.fusionStartDate(flight.getStartDate(), flight.getScheduledFlight().getStartTime(), null)
					.isBefore(OffsetDateTime.now().plusMinutes(BOOKING_LIMIT_MINUTES)
							.withOffsetSameLocal(ZoneOffset.of("+1")))) {
				// Too late: booking not possible anymore
				unlock();
				return 0;
			}

			Set<Traveler> travelers = new HashSet<>();
			Booking newBooking = Booking.builder().flight(flight).customerUserId(username).build();

			for (int i = 0; i < dto.getPassengers().length; i++) {
				IncomingTravelerDto travelerDto = dto.getPassengers()[i];
				SelectedSeatDto selectedSeat = dto.getSeats()[i];

				if (this.seatService.isSeatTaken(flight, selectedSeat)) {
					unlock();
					return 0;
				}

				Traveler traveler = Traveler.builder().firstName(travelerDto.getName())
						.lastName(travelerDto.getSurname()).checkedIn(travelerDto.isCheckedIn()).booking(newBooking)
						.seatRow(selectedSeat.getRow()).seatColumn(selectedSeat.getColumn()).build();
				travelers.add(traveler);

				Set<Luggage> luggages = new HashSet<>();
				if (allowedWeights.contains(travelerDto.getBaggage1())) {
					Luggage luggage1 = Luggage.builder().state(LuggageState.AT_TRAVELLER)
							.weight(travelerDto.getBaggage1()).traveler(traveler).build();
					luggages.add(luggage1);
				}

				if (allowedWeights.contains(travelerDto.getBaggage2())) {
					Luggage luggage2 = Luggage.builder().state(LuggageState.AT_TRAVELLER)
							.weight(travelerDto.getBaggage2()).traveler(traveler).build();
					luggages.add(luggage2);
				}
				if (allowedWeights.contains(travelerDto.getBaggage3())) {
					Luggage luggage3 = Luggage.builder().state(LuggageState.AT_TRAVELLER)
							.weight(travelerDto.getBaggage3()).traveler(traveler).build();
					luggages.add(luggage3);
				}
				if (allowedWeights.contains(travelerDto.getBaggage4())) {
					Luggage luggage4 = Luggage.builder().state(LuggageState.AT_TRAVELLER)
							.weight(travelerDto.getBaggage4()).traveler(traveler).build();
					luggages.add(luggage4);
				}
				if (!luggages.isEmpty()) {
					traveler.setLuggage(luggages);
				}
			}

			if (travelers.isEmpty()) {
				unlock();
				return 0;
			}
			newBooking.setTravelers(travelers);

			PriceCalculator.calculateFinalPrice(newBooking);

			// Generate temporary booking id for frontend
			int rand;
			do {
				rand = random.nextInt();
			} while (!bookingDrafts.containsKey(rand) && rand == 0);

			// Save draft
			currentUser = username;
			lastBookingDraft = OffsetDateTime.now();
			bookingDrafts.put(rand, newBooking);

			return rand;
		}
	}

	public boolean finishBooking(String username, Integer[] bookingIds) {
		synchronized (bookingDrafts) {
			if (currentUser != null && !currentUser.equals(username)) {
				// Locked by someone else
				return false;
			}

			// Additional list: user may have timed out and provide a invalid bookingId
			// Note: these ids don't correspond to the given id for the booking in the
			// database
			List<Booking> bookings = new ArrayList<>();

			// Get booking drafts
			for (int bookingId : bookingIds) {
				Booking draft = bookingDrafts.get(bookingId);
				if (draft != null) {
					bookings.add(draft);
				} else {
					return false;
				}
			}

			// Finally: save the bookings
			bookingRepository.saveAll(bookings);

			//send email to user
            var user = keycloakApiService.getUserById(username).get();
            try {
                mailNotificationService.sendInvoiceNotification(user.getEmail(), user.getFirstName() + " " + user.getLastName(), bookings);
            } catch (IOException e) {
                e.printStackTrace();
            }

            unlock();
		}

		return true;
	}

	private void unlock() {
		bookingDrafts.clear();
		currentUser = null;
	}
}
