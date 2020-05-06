package de.hso.badenair.service.plan.shift;

import de.hso.badenair.controller.dto.plan.RequestVacationDto;
import de.hso.badenair.domain.schedule.ShiftSchedule;
import de.hso.badenair.domain.schedule.Vacation;
import de.hso.badenair.service.plan.vacation.VacationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class ShiftPlanService {

    private final ShiftPlanRepository shiftPlanRepository;

    public List<ShiftSchedule> getShiftPlan(String employeeUserId) {
        return shiftPlanRepository.findByEmployeeUserIdOrderByStartTimeAsc(employeeUserId);
    }
}
