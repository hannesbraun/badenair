package de.hso.badenair.service.plan.shift;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import de.hso.badenair.domain.schedule.ShiftSchedule;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShiftPlanService {

	private final ShiftPlanRepository shiftPlanRepository;

	public List<ShiftSchedule> getShiftPlan(String employeeUserId) {
		List<ShiftSchedule> shiftPlan = shiftPlanRepository.findByEmployeeUserIdOrderByStartTimeAsc(employeeUserId);

		int currentMonth = OffsetDateTime.now().getMonthValue();
		int nextMonth = (currentMonth % 12) + 1;
		int currentMonthYear = OffsetDateTime.now().getYear();
		int nextMonthYear = currentMonth < 12 ? currentMonthYear : currentMonthYear + 1;

		if (OffsetDateTime.now().getDayOfMonth() < 10) {
			// Show plan for current
			return shiftPlan.stream().filter(shift -> shift.getStartTime().getMonthValue() == currentMonth
					&& shift.getStartTime().getYear() == currentMonthYear).collect(Collectors.toList());
		} else {
			// Also show plan for next month
			return shiftPlan.stream()
					.filter(shift -> (shift.getStartTime().getMonthValue() == currentMonth
							|| shift.getStartTime().getMonthValue() == nextMonth)
							&& (shift.getStartTime().getYear() == currentMonthYear
									|| shift.getStartTime().getYear() == nextMonthYear))
					.collect(Collectors.toList());
		}
	}
}
