package de.hso.badenair.util.time;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * Fusions the day and the time from a flight (provided by the database) into a single OffsetDateTime object.
 */
public class DateFusioner {
	/**
	 * Fusions the day and the time of a flight into a single OffsetDateTime
	 * object.
	 * 
	 * @param day
	 *            the day, month and year of the date
	 * @param time
	 *            the hour and minute
	 * @param targetTimezone
	 *            timezone of the fusioned date (supply null to keep the
	 *            timezone of the day object)
	 * @return the fusioned OffsetDateTime object
	 */
	public static OffsetDateTime fusionStartDate(OffsetDateTime day,
			OffsetDateTime time, String targetTimezone) {
		OffsetDateTime fusionedDate = day.withHour(time.getHour())
				.withMinute(time.getMinute());
		if (targetTimezone != null) {
			fusionedDate = fusionedDate
					.withOffsetSameInstant(ZoneOffset.of(targetTimezone));
		}

		return fusionedDate;
	}

	/**
	 * Fusions the day, time and duration of a flight into a single
	 * OffsetDateTime object.
	 * 
	 * @param day
	 *            the day, month and year of the date
	 * @param time
	 *            the hour and minute
	 * @param duration
	 *            the duration in hours
	 * @param targetTimezone
	 *            timezone of the fusioned date (supply null to keep the
	 *            timezone of the day object)
	 * @return the fusioned OffsetDateTime object
	 */
	public static OffsetDateTime fusionArrivalDate(OffsetDateTime day,
			OffsetDateTime time, double duration, String targetTimezone) {
		OffsetDateTime startDate = fusionStartDate(day, time, null);

		long durationFullHoursOnly = Double.valueOf(duration).intValue();

		OffsetDateTime fusionedDate = startDate.plusHours(durationFullHoursOnly)
				.plusMinutes(Double
						.valueOf((duration
								- Double.valueOf(durationFullHoursOnly)) * 60.0)
						.intValue());

		if (targetTimezone != null) {
			fusionedDate = fusionedDate
					.withOffsetSameInstant(ZoneOffset.of(targetTimezone));
		}

		return fusionedDate;
	}
}
