package de.hso.badenair.controller.flight;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

public class PriceCalculator {
	public static final int LUGGAGE_BASE_PRICE_PER_KG = 2;

	public static final long SECONDS_PER_WEEK = 604800;

	public static double calcFlightPriceRaw(double basePrice, int takenSeats,
			int totalSeats, OffsetDateTime startDate) {
		// Calculate demand factor
		final double demand = 1
				+ 0.5 * (1 - Math.exp(-3 * takenSeats / totalSeats));

		// Calculate time factor (and stay within the limits)
		final long twelveMonths = Duration.of(12, ChronoUnit.MONTHS)
				.getSeconds() / SECONDS_PER_WEEK;
		long timeLeft = Duration.between(OffsetDateTime.now(), startDate)
				.getSeconds() / SECONDS_PER_WEEK;
		long timeOver;
		if (timeLeft <= 0) {
			timeOver = twelveMonths;
		} else if (timeLeft > twelveMonths) {
			timeOver = 0;
		} else {
			timeOver = twelveMonths - timeLeft;
		}
		final double time = 1.0 + 0.5 * (1 - Math.exp(-3.0
				* Double.valueOf(timeOver) / Double.valueOf(twelveMonths)));

		// Round to two decimals
		return Math.round(basePrice * demand * time * 100.0) / 100.0;
	}
}
