package de.hso.badenair.controller.account;

import java.security.Principal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
import de.hso.badenair.domain.flight.Flight;
import de.hso.badenair.service.account.AccountService;
import de.hso.badenair.util.time.DateFusioner;
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
		List<BookingDto> bookingDtos = bookings.stream().map((booking) -> {
			// Travelers
			List<TravelerDto> travelerDtos = booking.getTravelers().stream()
					.map((traveler) -> {
						return new TravelerDto(
								traveler.getFirstName() + " "
										+ traveler.getLastName(),
								traveler.getId(), traveler.isCheckedIn());
					}).collect(Collectors.toList());

			// Luggage
			List<LuggageStateDto> luggageDtos = booking.getTravelers().stream()
					.flatMap(traveler -> traveler.getLuggage().stream())
					.map((luggage) -> {
						return new LuggageStateDto(luggage.getId(),
								luggage.getState());
					}).collect(Collectors.toList());

			Flight flight = booking.getFlight();

			// Dates
			OffsetDateTime startDate = DateFusioner.fusionStartDate(
					flight.getStartDate(),
					flight.getScheduledFlight().getStartTime(),
					flight.getScheduledFlight().getStartingAirport()
							.getTimezone());
			OffsetDateTime arrivalDate = DateFusioner.fusionArrivalDate(
					flight.getStartDate(),
					flight.getScheduledFlight().getStartTime(),
					flight.getScheduledFlight().getDurationInHours(),
					flight.getScheduledFlight().getStartingAirport()
							.getTimezone());

			// Create final booking dto
			return new BookingDto(new FlightDto(flight.getId(),
					flight.getScheduledFlight().getStartingAirport().getName(),
					flight.getScheduledFlight().getDestinationAirport()
							.getName(),
					startDate, arrivalDate, -1337.0), travelerDtos,
					luggageDtos);
		}).collect(Collectors.toList());

		return ResponseEntity.ok(bookingDtos);
	}
}
