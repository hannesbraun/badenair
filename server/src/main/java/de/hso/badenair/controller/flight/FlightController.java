package de.hso.badenair.controller.flight;


import de.hso.badenair.service.flight.FlightService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employee/flight")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    @PatchMapping("/tracking/{id}")
    public ResponseEntity<?>  updateFlightTracking (@PathVariable long id, @RequestBody String action){
        boolean updateSuccess = flightService.updateFlightTracking(id, action);

        if (updateSuccess) {
            return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
        } else {
            // The only possible failure is probably that the luggage id isn't
            // present in the database
            return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
        }
    }
}
