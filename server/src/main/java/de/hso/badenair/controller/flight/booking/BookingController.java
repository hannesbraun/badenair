package de.hso.badenair.controller.flight.booking;

import de.hso.badenair.controller.dto.flight.IncomingBookingDto;
import de.hso.badenair.service.flight.booking.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/customer/flight")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping("/booking")
    public ResponseEntity<?> bookFlight(Principal user, @RequestBody @Valid IncomingBookingDto dto){
        boolean success = bookingService.bookFlight(user.getName(), dto);

        if(success){
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

}
