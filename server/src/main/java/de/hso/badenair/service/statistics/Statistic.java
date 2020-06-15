package de.hso.badenair.service.statistics;

import de.hso.badenair.domain.booking.Booking;
import de.hso.badenair.domain.flight.Flight;
import de.hso.badenair.domain.plane.PlaneType;
import de.hso.badenair.service.flight.repository.FlightRepository;
import de.hso.badenair.util.time.DateFusioner;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class Statistic {
    private final FlightRepository flightRepository;

    //@Scheduled (fixedRate = 86_400_000)
    @Scheduled (fixedRate = 10_000)
    @Transactional
    public void saveStatistic()
    {
        System.out.println("Generating Statistics");
        OffsetDateTime startDate = OffsetDateTime.now().withSecond(0).withMinute(0).withHour(0);

		List<Flight> flights = flightRepository.findByStartDateAfter(startDate);

        for(int i = 0; i < flights.size(); i++) {
            OffsetDateTime startTime = DateFusioner.fusionStartDate(flights.get(i).getStartDate(),
                flights.get(i).getScheduledFlight().getStartTime(),
                null);
            OffsetDateTime landingTime = DateFusioner.fusionArrivalDate(flights.get(i).getStartDate(),
                flights.get(i).getScheduledFlight().getStartTime(),
                flights.get(i).getScheduledFlight().getDurationInHours(),
                null);
            Long id = flights.get(i).getId();

			double sales = 0;
			Set<Booking> bookings = flights.get(i).getBookings();

			for (Booking b : bookings) {
				sales += b.getPrice();
			}

			PlaneType planeType = flights.get(i).getPlane().getTypeData().getType();

            try {
                FileWriter fileWriter = new FileWriter("statistics.txt", false);
                PrintWriter printWriter = new PrintWriter(fileWriter);
                printWriter.printf("Statistics for %s\n\nPlaneID:\t\t%d\nPlaneType:\t\t%s\nStartTime:\t\t%s\nLandingTime:\t%s\nSales:\t\t\t%f\n",
                    OffsetDateTime.now().toString(),
                    id,
                    planeType.toString(),
                    startTime.toString(),
                    landingTime.toString(),
                    sales);

				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
