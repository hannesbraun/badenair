package de.hso.badenair.controller.flightplan;

import de.hso.badenair.controller.dto.flightplan.PlaneScheduleDto;
import de.hso.badenair.controller.dto.flightplan.FlightDto;
import de.hso.badenair.service.flightplan.FlightplanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

import java.time.OffsetDateTime;

@RequiredArgsConstructor
public class FlightplanController {
    private final FlightplanService flightplanService;

    @GetMapping
<<<<<<< Updated upstream:server/src/main/java/de/hso/badenair/controller/flightplan/FlightController.java
    public ResponseEntity<PlaneScheduleDto[]> getPlaneSchedules() {
<<<<<<< Updated upstream:server/src/main/java/de/hso/badenair/controller/flightplan/FlightplanController.java
        return null;
=======
        FlightDto temp1 = new FlightDto();
        PlaneScheduleDto temp = new PlaneScheduleDto();
    
        PlaneScheduleDto test[];

        return ResponseEntity.ok(test);
=======
    public ResponseEntity<ArrayList<PlaneScheduleDto>> getPlaneSchedules() {


        return ResponseEntity.ok(flightplanService.getPlaneSchedules());
>>>>>>> Stashed changes:server/src/main/java/de/hso/badenair/controller/flightplan/FlightplanController.java
>>>>>>> Stashed changes:server/src/main/java/de/hso/badenair/controller/flightplan/FlightController.java
    }
}
