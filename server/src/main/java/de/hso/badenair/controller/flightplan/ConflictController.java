package de.hso.badenair.controller.flightplan;

import de.hso.badenair.controller.dto.flightplan.ConflictDto;
import de.hso.badenair.controller.dto.flightplan.PlaneScheduleDto;
import de.hso.badenair.service.flightplan.FlightPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/employee/flightplan/conflicts")
@RequiredArgsConstructor
public class ConflictController {
    private final FlightPlanService flightplanService;

    @GetMapping
    public ResponseEntity<List<ConflictDto>> getPlaneSchedules() {
        return ResponseEntity.ok(flightplanService.getConflicts());
    }
}
