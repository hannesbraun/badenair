package de.hso.badenair.controller.flight;


import de.hso.badenair.controller.dto.flight.FlightDto;
import de.hso.badenair.controller.dto.flight.TrackingDto;
import de.hso.badenair.service.flight.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api/employee/flight")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    @PatchMapping("/tracking/{id}")
    public ResponseEntity<OffsetDateTime>  updateFlightTracking (@PathVariable Long id, @RequestBody TrackingDto dto){
        OffsetDateTime updateSuccess = flightService.updateFlightTracking(id, dto);

        if (updateSuccess != null) {
            return ResponseEntity.ok(updateSuccess);
        } else {
            return new ResponseEntity<OffsetDateTime>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tracking/action/{id}")
    public ResponseEntity<TrackingDto> getFlightAction(@PathVariable Long id){
        TrackingDto resultDto= flightService.getFlightAction(id);

        if(resultDto != null){
            return ResponseEntity.ok(resultDto);
        }
        else{
            return new ResponseEntity<TrackingDto>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/pilot")
    public ResponseEntity<FlightDto> getCurrentFlightForPilot(Principal user){
        FlightDto result= flightService.getCurrentFlightForPilot(user.getName());

        if(result != null){
            return ResponseEntity.ok(result);
        }
        else {
            return new ResponseEntity<FlightDto>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
