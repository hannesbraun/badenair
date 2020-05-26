package de.hso.badenair.controller.maintenance;

import de.hso.badenair.controller.dto.maintenance.PlaneMaintenanceDto;
import de.hso.badenair.domain.plane.Plane;
import de.hso.badenair.util.mapper.PlaneMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import de.hso.badenair.service.maintenance.MaintenanceService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class MaintenanceController {

    private final MaintenanceService maintenanceService;


    @PatchMapping("/maintenance/{id}")
    public ResponseEntity<Object> updateMaintenanceState(@PathVariable Long id) {
        boolean updateSuccess = maintenanceService.updateMaintenanceState(id);

        if (updateSuccess) {
            return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
        } else {
            // The only possible failure is probably that the planeId isn't
            // present in the database
            return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/maintenance")
    public ResponseEntity<List<PlaneMaintenanceDto>> getMaintenance() {
        final List<PlaneMaintenanceDto> dtos = this.maintenanceService.getAllPlanes().stream()
            .map(PlaneMapper::mapToDto)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

}
