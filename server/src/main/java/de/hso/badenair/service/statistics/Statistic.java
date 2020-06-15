package de.hso.badenair.service.statistics;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.hso.badenair.domain.booking.Booking;
import de.hso.badenair.domain.flight.Flight;
import de.hso.badenair.domain.plane.PlaneType;
import de.hso.badenair.service.flight.repository.FlightRepository;
import de.hso.badenair.util.time.DateFusioner;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Statistic {
	private final FlightRepository flightRepository;

	@Scheduled(cron = "0 0 0 * * ?")
	@Transactional
	public void saveStatistic() {
		double totalSales = 0;
		OffsetDateTime startDate = OffsetDateTime.now().withNano(0).withSecond(0).withMinute(0).withHour(0)
				.minusDays(1l).withOffsetSameLocal(ZoneOffset.of("+1"));

		// Get flights (getting a bit more flights from the database to avoid timezone
		// problems, filtering the wrong ones out afterwards)
		List<Flight> flights = flightRepository.findByActualLandingTimeBetween(startDate.minusDays(1l),
				startDate.plusDays(2l));
		flights = flights.stream()
				.filter(flight -> flight.getActualLandingTime() != null
						&& flight.getActualLandingTime().isAfter(startDate)
						&& flight.getActualLandingTime().isBefore(startDate.plusDays(1)))
				.collect(Collectors.toList());

		try {
			FileWriter fileWriter = new FileWriter(
					"statistics-" + startDate.format(DateTimeFormatter.ofPattern("YYYY-MM-dd")) + ".txt", false);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			printWriter.print(
					"  ____            _                     _\n" + " |  _ \\          | |              /\\   (_)\n"
							+ " | |_) | __ _  __| | ___ _ __    /  \\   _ _ __\n"
							+ " |  _ < / _` |/ _` |/ _ \\ '_ \\  / /\\ \\ | | '__|\n"
							+ " | |_) | (_| | (_| |  __/ | | |/ ____ \\| | |\n"
							+ " |____/ \\__,_|\\__,_|\\___|_| |_/_/    \\_\\_|_|\n\n\n");
			printWriter.printf("Statistics for %s\n\n", startDate.format(DateTimeFormatter.ofPattern("YYYY-MM-dd")));

			for (Flight flight : flights) {
				OffsetDateTime scheduledStartTime = DateFusioner
						.fusionStartDate(flight.getStartDate(), flight.getScheduledFlight().getStartTime(), null)
						.withSecond(0).withNano(0);
				OffsetDateTime scheduledLandingTime = DateFusioner
						.fusionArrivalDate(flight.getStartDate(), flight.getScheduledFlight().getStartTime(),
								flight.getScheduledFlight().getDurationInHours(), null)
						.withSecond(0).withNano(0);
				OffsetDateTime actualStartTime = flight.getActualStartTime();
				OffsetDateTime actualLandingTime = flight.getActualLandingTime();

				// Calculate delay
				long takeoffDelay = 0;
				if (actualStartTime != null) {
					takeoffDelay = Duration.between(scheduledStartTime, actualStartTime).getSeconds() / 60;
				}
				long janDelay = 0; // Wait... what? Lul :D
				if (actualLandingTime != null) {
					janDelay = Duration.between(scheduledLandingTime, actualLandingTime).getSeconds() / 60;
				}

				// Plane id
				Long id = flight.getPlane().getId();

				// Calculate sales
				double sales = 0;
				Set<Booking> bookings = flight.getBookings();

				for (Booking b : bookings) {
					sales += b.getPrice();
				}
				totalSales += sales;

				// Plane type
				PlaneType planeType = flight.getPlane().getTypeData().getType();

				// Write flight to file
				printWriter.printf(
						"PlaneID:        %d\nPlaneType:      %s\nStartTime:      %s\nDelay:          %s minutes\nLandingTime:    %s\nDelay:          %s minutes\nSales:          %.2f euros\n\n",
						id, planeType.toString(),
						actualStartTime != null ? actualStartTime.toString() : "no data available", takeoffDelay,
						actualLandingTime != null ? actualLandingTime.toString() : "no data available", janDelay,
						sales);
			}
			printWriter.printf("\nTotal sales: %f\n", totalSales);

			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
