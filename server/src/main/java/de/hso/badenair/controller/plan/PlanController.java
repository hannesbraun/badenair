package de.hso.badenair.controller.plan;

import de.hso.badenair.domain.schedule.StandbySchedule;
import de.hso.badenair.service.plan.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/employee/plan")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @RequestMapping("/standby")
    public List<StandbySchedule> getStandbyPlan() {
        return this.planService.getStandbyPlan();
    }
}
