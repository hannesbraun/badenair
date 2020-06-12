package de.hso.badenair.controller.plan;

import java.security.Principal;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.hso.badenair.controller.dto.plan.RequestVacationDto;
import de.hso.badenair.controller.dto.plan.ShiftDto;
import de.hso.badenair.controller.dto.plan.VacationDto;
import de.hso.badenair.controller.dto.plan.VacationPlanDto;
import de.hso.badenair.domain.schedule.StandbySchedule;
import de.hso.badenair.service.plan.PlanService;
import de.hso.badenair.service.plan.shift.ShiftPlanService;
import de.hso.badenair.service.plan.vacation.VacationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/employee/plan")
@RequiredArgsConstructor
public class PlanController {

	private final PlanService planService;
	private final VacationService vacationService;
	private final ShiftPlanService shiftPlanService;

	@GetMapping("/shift")
	public ResponseEntity<List<ShiftDto>> getShiftPlan(Principal user) {
		final List<ShiftDto> shiftDtos = shiftPlanService.getShiftPlan(user.getName()).stream()
				.map(shift -> new ShiftDto(
						shift.getStartTime().withOffsetSameLocal(
								ZoneOffset.of(TimeZone.getDefault().inDaylightTime(new Date()) ? "+2" : "+1")),
						shift.getEndTime().withOffsetSameLocal(
								ZoneOffset.of(TimeZone.getDefault().inDaylightTime(new Date()) ? "+2" : "+1"))))
				.collect(Collectors.toList());
		return ResponseEntity.ok(shiftDtos);
	}

	@GetMapping("/vacation")
	public ResponseEntity<VacationPlanDto> getVacationPlan(Principal user) {
		final String userName = user.getName();
		final List<VacationDto> vacationDtos = vacationService.getVacation(userName).stream()
				.map(vacation -> new VacationDto(
						vacation.getStartTime().withOffsetSameLocal(
								ZoneOffset.of(TimeZone.getDefault().inDaylightTime(new Date()) ? "+2" : "+1")),
						vacation.getEndTime().withOffsetSameLocal(
								ZoneOffset.of(TimeZone.getDefault().inDaylightTime(new Date()) ? "+2" : "+1"))))
				.collect(Collectors.toList());

		final VacationPlanDto dto = new VacationPlanDto(vacationDtos,
				vacationService.getRemainingVacationDays(userName));

		return ResponseEntity.ok(dto);
	}

	@PostMapping("/vacation")
	public ResponseEntity<?> requestVacation(Principal user, @RequestBody @Valid RequestVacationDto dto) {
		vacationService.requestVacation(user.getName(), dto);

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("/standby")
	public ResponseEntity<List<StandbySchedule>> getStandbyPlan() {
		List<StandbySchedule> standbyScheduleList = this.planService.getStandbyPlan();
		standbyScheduleList.stream().forEach(standbySchedule -> {
			standbySchedule.setStartTime(standbySchedule.getStartTime().withOffsetSameLocal(
					ZoneOffset.of(TimeZone.getDefault().inDaylightTime(new Date()) ? "+2" : "+1")));
			standbySchedule.setEndTime(standbySchedule.getEndTime().withOffsetSameLocal(
					ZoneOffset.of(TimeZone.getDefault().inDaylightTime(new Date()) ? "+2" : "+1")));
		});
		return ResponseEntity.ok(standbyScheduleList);
	}
}
