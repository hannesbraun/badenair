package de.hso.badenair.controller.account;

import java.security.Principal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.hso.badenair.controller.dto.account.AccountDataDto;
import de.hso.badenair.controller.dto.account.UpdateAccountDataDto;
import de.hso.badenair.controller.dto.booking.BookingDto;
import de.hso.badenair.controller.dto.flight.FlightDto;
import de.hso.badenair.controller.dto.luggage.LuggageStateDto;
import de.hso.badenair.controller.dto.traveler.TravelerDto;
import de.hso.badenair.domain.booking.Booking;
import de.hso.badenair.domain.booking.Luggage;
import de.hso.badenair.domain.booking.Traveler;
import de.hso.badenair.service.account.AccountService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customer/account")
@RequiredArgsConstructor
public class AccountController {

	private final AccountService accountService;

	@GetMapping
	public ResponseEntity<AccountDataDto> getAccountData(Principal user) {
		return ResponseEntity.ok(accountService.getAccountData(user.getName()));
	}

	@PutMapping
	public ResponseEntity<?> updateAccountData(Principal user,
			@RequestBody @Valid UpdateAccountDataDto dto) {
		accountService.updateAccountData(user.getName(), dto);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/flights")
	public ResponseEntity<List<BookingDto>> getBookings(Principal user) {
		List<Booking> bookings = accountService.getBookings(user.getName());
		List<BookingDto> bookingDtos = new ArrayList<BookingDto>();

		for (Booking booking : bookings) {
			// Travelers and luggage
			List<TravelerDto> travelerDtos = new ArrayList<TravelerDto>();
			List<LuggageStateDto> luggageDtos = new ArrayList<LuggageStateDto>();
			for (Traveler traveler : booking.getTravelers()) {
				travelerDtos.add(new TravelerDto(
						traveler.getFirstName() + " " + traveler.getLastName(),
						traveler.getId(), traveler.isCheckedIn()));

				for (Luggage luggage : traveler.getLuggage()) {
					luggageDtos.add(new LuggageStateDto(luggage.getId(),
							luggage.getState()));
				}
			}

			// Dates
			long durationInHours = Double.valueOf(booking.getFlight()
					.getScheduledFlight().getDurationInHours()).intValue();
			OffsetDateTime startDate = booking.getFlight().getStartDate()
					.plusHours(booking.getFlight().getScheduledFlight()
							.getStartTime().getHour())
					.plusMinutes(booking.getFlight().getScheduledFlight()
							.getStartTime().getMinute())
					.withOffsetSameInstant(ZoneOffset
							.of(booking.getFlight().getScheduledFlight()
									.getStartingAirport().getTimezone()));
			OffsetDateTime arrivalDate = startDate.plusHours(durationInHours)
					.plusMinutes(Double
							.valueOf((booking.getFlight().getScheduledFlight()
									.getDurationInHours()
									- Double.valueOf(durationInHours)) * 60.0)
							.intValue())
					.withOffsetSameInstant(ZoneOffset
							.of(booking.getFlight().getScheduledFlight()
									.getDestinationAirport().getTimezone()));

			BookingDto bookingDto = new BookingDto(
					new FlightDto(booking.getFlight().getId(),
							booking.getFlight().getScheduledFlight()
									.getStartingAirport().getName(),
							booking.getFlight().getScheduledFlight()
									.getDestinationAirport().getName(),
							startDate, arrivalDate, -1337.0),
					travelerDtos, luggageDtos);
			bookingDtos.add(bookingDto);
		}

		return ResponseEntity.ok(bookingDtos);
	}
}
