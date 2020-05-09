package de.hso.badenair.controller.flightplan;

import de.hso.badenair.controller.dto.flightplan.PlaneScheduleDto;
import de.hso.badenair.controller.dto.flightplan.FlightDto;
import de.hso.badenair.service.flightplan.FlightplanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api/employee/flightplan")
@RequiredArgsConstructor
public class FlightplanController {
    private final FlightplanService flightplanService;

    @GetMapping
    public ResponseEntity<ArrayList<PlaneScheduleDto>> getPlaneSchedules() {


        return ResponseEntity.ok(flightplanService.getPlaneSchedules());
    }
}
