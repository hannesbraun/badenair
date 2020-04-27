package de.hso.badenair.controller.flight;


import de.hso.badenair.service.flight.FlightService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api/employee/flight")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    @PatchMapping("/tracking/{id}")
    public ResponseEntity<OffsetDateTime>  updateFlightTracking (@PathVariable Long id, @RequestBody String action){
        OffsetDateTime updateSuccess = flightService.updateFlightTracking(id, action);

        if (updateSuccess != null) {
            return ResponseEntity.ok(updateSuccess);
        } else {
            return new ResponseEntity<OffsetDateTime>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
