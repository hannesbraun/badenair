package de.hso.badenair.controller.flightplan;

import de.hso.badenair.controller.dto.flightplan.ConflictDto;
import de.hso.badenair.controller.dto.flightplan.PlaneScheduleDto;
import de.hso.badenair.controller.dto.flightplan.ReservePlaneSolutionDto;
import de.hso.badenair.service.flightplan.FlightPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/api/employee/flightplan/conflicts")
@RequiredArgsConstructor
public class ConflictController {
    private final FlightPlanService flightplanService;

    @GetMapping
    public ResponseEntity<List<ConflictDto>> getConflicts() {
        return ResponseEntity.ok(flightplanService.getConflicts());
    }

    @PostMapping("/useReservePlane")
    public ResponseEntity<?> useReservePlane(@RequestBody ReservePlaneSolutionDto data){
        flightplanService.resolvePlaneConflict(data.getFlightID(), data.getReservePlaneID());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/cancelFlight/{flightID}")
    public ResponseEntity<?> cancelFlight(@PathVariable long flightID){
        flightplanService.cancelFlight(flightID);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/ignoreDelay/{flightID}")
    public ResponseEntity<?> ignoreDelay(@PathVariable long flightID){
        flightplanService.ignoreDelay(flightID);

        return ResponseEntity.ok().build();
    }
}
