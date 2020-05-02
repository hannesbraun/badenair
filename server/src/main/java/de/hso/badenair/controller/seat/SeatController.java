package de.hso.badenair.controller.seat;

import de.hso.badenair.controller.dto.seat.SeatDto;
import de.hso.badenair.service.seat.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customer/")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    @GetMapping("/seat")
    public ResponseEntity<List<SeatDto>> getSeats(@RequestParam int flightId) {
        final List<SeatDto> seats = this.seatService.getSeatsByFlightId(flightId);
        return ResponseEntity.ok(seats);
    }
}
