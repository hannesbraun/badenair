package de.hso.badenair.service.flightplan;

import de.hso.badenair.controller.dto.flightplan.FlightWithoutPriceDto;
import de.hso.badenair.controller.dto.flightplan.PlaneScheduleDto;
import de.hso.badenair.domain.flight.ScheduledFlight;
import de.hso.badenair.domain.plane.Plane;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlightPlanService {

    private final FlightplanRepository flightplanRepository;

    public ArrayList<PlaneScheduleDto> getPlaneSchedules() {
        OffsetDateTime now = OffsetDateTime.now();
        now = now.minusHours(1);
        OffsetDateTime oneDayLater = now.plusHours(12);

        List<Plane> planes = flightplanRepository.findByActualStartTimeBetween(now, oneDayLater);
        ArrayList<PlaneScheduleDto> planeSchedules = new ArrayList<>(0);

        planes.forEach(plane -> {
            final List<FlightWithoutPriceDto> flights = plane.getFlight().stream()
                .map(flight -> {
                    final ScheduledFlight scheduledFlight = flight.getScheduledFlight();
                    return new FlightWithoutPriceDto(flight.getId(),
                        scheduledFlight.getStartingAirport().getName(),
                        scheduledFlight.getDestinationAirport().getName(),
                        flight.getActualStartTime(),
                        flight.getActualLandingTime());
                })
                .collect(Collectors.toList());

            planeSchedules.add(new PlaneScheduleDto(plane.getId(),
                plane.getTypeData().getType().getName(),
                plane.getState().getName(),
                false,
                flights));
        });

        return planeSchedules;
    }
}
