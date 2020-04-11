package de.hso.badenair.controller;

import de.hso.badenair.domain.schedule.StandbySchedule;
import de.hso.badenair.service.plane.repository.StandbyScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/employee/plan")
public class PlanController {

    @Autowired
    private StandbyScheduleRepository standbyScheduleRepository;

    @RequestMapping("/standby")
    public List<StandbySchedule> getStandbyPlan() {
        return (List<StandbySchedule>) standbyScheduleRepository.findAll();
    }
}
