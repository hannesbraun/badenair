package de.hso.badenair.controller.flightplan;

import de.hso.badenair.controller.dto.flightplan.PlaneScheduleDto;
import de.hso.badenair.controller.dto.flightplan.FlightDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api/employee/flightplan")
@RequiredArgsConstructor
public class FlightController {
    @GetMapping
    public ResponseEntity<PlaneScheduleDto[]> getPlaneSchedules() {
        FlightDto temp1 = new FlightDto();
        PlaneScheduleDto temp = new PlaneScheduleDto();
    
        PlaneScheduleDto test[];

        return ResponseEntity.ok(test);
    }
}