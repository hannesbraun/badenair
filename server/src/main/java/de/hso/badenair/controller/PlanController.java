package de.hso.badenair.controller;

import de.hso.badenair.controller.dto.plan.RequestVacationDto;
import de.hso.badenair.controller.dto.plan.VacationDto;
import de.hso.badenair.service.plan.vacation.VacationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employee/plan")
@RequiredArgsConstructor
public class PlanController {

    private final VacationService vacationService;

    @GetMapping("/vacation")
    public ResponseEntity<List<VacationDto>> getVacationPlan(Principal user) {
        final List<VacationDto> vacationDtos = vacationService.getVacation(user.getName()).stream()
            .map(vacation -> new VacationDto(vacation.getStartTime(), vacation.getEndTime()))
            .collect(Collectors.toList());

        return ResponseEntity.ok(vacationDtos);
    }

    @PostMapping("/vacation")
    public ResponseEntity<?> requestVacation(Principal user, @RequestBody @Valid RequestVacationDto dto) {
        vacationService.requestVacation(user.getName(), dto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
