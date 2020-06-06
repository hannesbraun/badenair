package de.hso.badenair.controller.flight.booking;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.hso.badenair.controller.dto.flight.IncomingBookingDto;
import de.hso.badenair.service.flight.booking.BookingService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customer/flight")
@RequiredArgsConstructor
public class BookingController {
	private final BookingService bookingService;

	@PostMapping("/booking")
	public ResponseEntity<Integer> bookFlight(Principal user, @RequestBody @Valid IncomingBookingDto dto) {
		int id = bookingService.bookFlight(user.getName(), dto);

		if (id != 0) {
			return ResponseEntity.ok(id);
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

	@PostMapping("/confirmBooking")
	public ResponseEntity<?> confirmBooking(Principal user, @RequestBody @Valid Integer[] bookingIds) {
		boolean success = bookingService.finishBooking(user.getName(), bookingIds);

		if (success) {
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

}
