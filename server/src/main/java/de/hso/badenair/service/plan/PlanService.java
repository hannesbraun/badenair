package de.hso.badenair.service.plan;

import de.hso.badenair.domain.schedule.ShiftSchedule;
import de.hso.badenair.domain.schedule.StandbySchedule;
import de.hso.badenair.service.plan.repository.StandbyScheduleRepository;
import de.hso.badenair.service.plan.shift.ShiftPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService {
    private final StandbyScheduleRepository standbyScheduleRepository;
    private final ShiftPlanRepository shiftPlanRepository;

    public List<StandbySchedule> getStandbyPlan() {
        return (List<StandbySchedule>) standbyScheduleRepository.findAll();
    }

    public List<ShiftSchedule> getShiftPlan() {
        return (List<ShiftSchedule>) shiftPlanRepository.findAll();
    }
}
