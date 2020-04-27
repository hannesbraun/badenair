package de.hso.badenair.service.plan;

import de.hso.badenair.domain.schedule.StandbySchedule;
import de.hso.badenair.service.plan.repository.StandbyScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final StandbyScheduleRepository standbyScheduleRepository;

    public List<StandbySchedule> getStandbyPlan() {
        return (List<StandbySchedule>) standbyScheduleRepository.findAll();
    }
}
