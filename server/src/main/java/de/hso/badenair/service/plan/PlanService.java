package de.hso.badenair.service.plan;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import de.hso.badenair.domain.schedule.ShiftSchedule;
import de.hso.badenair.domain.schedule.StandbySchedule;
import de.hso.badenair.service.plan.repository.StandbyScheduleRepository;
import de.hso.badenair.service.plan.shift.ShiftPlanRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlanService {
	private final StandbyScheduleRepository standbyScheduleRepository;
	private final ShiftPlanRepository shiftPlanRepository;

	public List<StandbySchedule> getStandbyPlan() {
		List<StandbySchedule> standbyPlan = (List<StandbySchedule>) standbyScheduleRepository.findAll();

		int currentMonth = OffsetDateTime.now().getMonthValue();
		int nextMonth = (currentMonth % 12) + 1;
		int currentMonthYear = OffsetDateTime.now().getYear();
		int nextMonthYear = currentMonth < 12 ? currentMonthYear : currentMonthYear + 1;

		if (OffsetDateTime.now().getDayOfMonth() < 10) {
			// Show plan for current
			return standbyPlan.stream().filter(standby -> standby.getStartTime().getMonthValue() == currentMonth
					&& standby.getStartTime().getYear() == currentMonthYear).collect(Collectors.toList());
		} else {
			// Also show plan for next month
			return standbyPlan.stream()
					.filter(standby -> (standby.getStartTime().getMonthValue() == currentMonth
							|| standby.getStartTime().getMonthValue() == nextMonth)
							&& (standby.getStartTime().getYear() == currentMonthYear
									|| standby.getStartTime().getYear() == nextMonthYear))
					.collect(Collectors.toList());
		}
	}

	public List<ShiftSchedule> getShiftPlan() {
		return (List<ShiftSchedule>) shiftPlanRepository.findAll();
	}
}
