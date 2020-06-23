package de.hso.badenair.util.init;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public class CrewData {
	@Getter
	List<String> employees = new ArrayList<>();

	@Setter
	OffsetDateTime busyUntil = null;

    int assignments = 0;

	private Map<String, Double> hoursPerDay = new HashMap<>();

	public void addHourAtDay(OffsetDateTime day, double hours) {
		String dayString = getDayStringFrom(day);

		if (hoursPerDay.containsKey(dayString)) {
			hoursPerDay.put(dayString, hoursPerDay.get(dayString) + hours);
		} else {
			hoursPerDay.put(dayString, hours);
		}
	}

	public double getHoursAtDay(OffsetDateTime day) {
		String dayString = getDayStringFrom(day);

		if (hoursPerDay.containsKey(dayString)) {
			return hoursPerDay.get(dayString);
		} else {
			return 0.0;
		}
	}

	private String getDayStringFrom(OffsetDateTime day) {
		return day.format(DateTimeFormatter.ofPattern("dd-MM-YYY"));
	}
}
