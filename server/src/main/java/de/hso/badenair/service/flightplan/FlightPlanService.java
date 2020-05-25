package de.hso.badenair.service.flightplan;

import de.hso.badenair.controller.dto.flightplan.ConflictDto;
import de.hso.badenair.controller.dto.flightplan.FlightWithoutPriceDto;
import de.hso.badenair.controller.dto.flightplan.PlaneScheduleDto;
import de.hso.badenair.domain.flight.ScheduledFlight;
import de.hso.badenair.domain.plane.Plane;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlightPlanService {

    private final FlightplanRepository flightplanRepository;
    private ArrayList<ConflictDto> conflicts;

    public ArrayList<PlaneScheduleDto> getPlaneSchedules() {
        OffsetDateTime now = OffsetDateTime.now();
        now = now.minusHours(1);
        OffsetDateTime oneDayLater = now.plusHours(12);
        AtomicBoolean hasConflict = new AtomicBoolean(false);

        List<Plane> planes = flightplanRepository.findByActualStartTimeBetween(now, oneDayLater);
        ArrayList<PlaneScheduleDto> planeSchedules = new ArrayList<>(0);

        ConflictFinder.findConflicts(planes, conflicts);

        planes.forEach(plane -> {
            hasConflict.set(false);
            final List<FlightWithoutPriceDto> flights = plane.getFlight().stream()
                .map(flight -> {
                    final ScheduledFlight scheduledFlight = flight.getScheduledFlight();

                    for (int i = 0; i < conflicts.size(); i++)
                    {
                        if (flight.getId() == conflicts.get(i).getFlightID())
                            hasConflict.set(true);
                    }

                    return new FlightWithoutPriceDto(flight.getId(),
                        scheduledFlight.getStartingAirport().getName(),
                        scheduledFlight.getDestinationAirport().getName(),
                        flight.getActualStartTime(),
                        flight.getActualLandingTime(),
                        flight.getStartDate(),
                        flight.getStartDate().plusHours(flight.getScheduledFlight().getDurationInHours().longValue()));
                })
                .collect(Collectors.toList());



            planeSchedules.add(new PlaneScheduleDto(plane.getId(),
                plane.getTypeData().getType().getName(),
                plane.getState().getName(),
                hasConflict.get(),
                flights));
        });

        return planeSchedules;
    }

    public ArrayList<ConflictDto> getConflicts() {
        return conflicts;
    }
}
