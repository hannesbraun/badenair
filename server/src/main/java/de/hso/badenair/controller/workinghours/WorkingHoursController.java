package de.hso.badenair.controller.workinghours;

import de.hso.badenair.controller.dto.plan.WorkingHoursDto;
import de.hso.badenair.domain.schedule.WorkingHours;
import de.hso.badenair.service.workinghours.WorkingHoursService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class WorkingHoursController {

    private final WorkingHoursService workingHoursService;

    @GetMapping("/workinghours")
    public ResponseEntity<WorkingHoursDto> getLatestWorkingHours(Principal user) {
        Optional<WorkingHours> latestWorkingHours = workingHoursService.getLatestWorkingHours(user.getName());

        if(latestWorkingHours.isEmpty()) {
            return ResponseEntity.ok(null);
        } else {
            WorkingHours workingHours = latestWorkingHours.get();

            return ResponseEntity.ok(new WorkingHoursDto(workingHours.getStartTime(), workingHours.getEndTime()));
        }
    }

    @PostMapping("/workinghours")
    public ResponseEntity<WorkingHoursDto> triggerWorkingHours(Principal user)  {
        WorkingHours workingHours = workingHoursService.triggerWorkingHours(user.getName());

        return ResponseEntity.ok(new WorkingHoursDto(workingHours.getStartTime(), workingHours.getEndTime()));
    }
}
