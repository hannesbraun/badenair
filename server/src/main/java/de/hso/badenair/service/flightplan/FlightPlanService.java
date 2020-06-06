package de.hso.badenair.service.flightplan;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import de.hso.badenair.controller.dto.flightplan.ConflictDto;
import de.hso.badenair.controller.dto.flightplan.FlightWithoutPriceDto;
import de.hso.badenair.controller.dto.flightplan.PlaneScheduleDto;
import de.hso.badenair.domain.flight.ScheduledFlight;
import de.hso.badenair.domain.plane.Plane;
import de.hso.badenair.service.plane.repository.PlaneRepository;
import de.hso.badenair.util.time.DateFusioner;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FlightPlanService {

	private final FlightplanRepository flightplanRepository;
	private final PlaneRepository planeRepository;
	private ArrayList<ConflictDto> conflicts = new ArrayList<>();

	public ArrayList<PlaneScheduleDto> getPlaneSchedules() {
		OffsetDateTime now = OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.of("+1"));
		now = now.truncatedTo(ChronoUnit.DAYS);
		now = now.plusHours(6);
		OffsetDateTime oneDayLater = now.plusHours(21);
		AtomicBoolean hasConflict = new AtomicBoolean(false);

		List<Plane> planes = (List<Plane>) planeRepository.findAll();
		ArrayList<PlaneScheduleDto> planeSchedules = new ArrayList<>(0);

		OffsetDateTime finalNow = now;
		planes.forEach(plane -> {
			hasConflict.set(false);

			final List<FlightWithoutPriceDto> flights = flightplanRepository
					.findByStartDateBetweenAndPlane_IdEquals(finalNow.withHour(0).withMinute(0).withSecond(0),
							oneDayLater.withHour(23).withMinute(59).withSecond(59), plane.getId())
					.stream().filter(flight -> {
						OffsetDateTime fusionedStartDate = DateFusioner.fusionStartDate(flight.getStartDate(),
								flight.getScheduledFlight().getStartTime(), null);
						return fusionedStartDate.isAfter(finalNow) && fusionedStartDate.isBefore(oneDayLater);
					})

					.map(flight -> {
						final ScheduledFlight scheduledFlight = flight.getScheduledFlight();

						for (int i = 0; i < conflicts.size(); i++) {
							if (flight.getId() == conflicts.get(i).getFlightID())
								hasConflict.set(true);
						}

						// Adjusting timezones in these dtos again because the frontend wants another
						// timezone :(
						return new FlightWithoutPriceDto(flight.getId(), scheduledFlight.getStartingAirport().getName(),
								scheduledFlight.getDestinationAirport().getName(),
								DateFusioner
										.fusionStartDate(flight.getStartDate(), scheduledFlight.getStartTime(), null)
										.withOffsetSameLocal(ZoneOffset
												.of(TimeZone.getDefault().inDaylightTime(new Date()) ? "+2" : "+1")),
								DateFusioner
										.fusionArrivalDate(flight.getStartDate(), scheduledFlight.getStartTime(),
												scheduledFlight.getDurationInHours(), null)
										.withOffsetSameLocal(ZoneOffset
												.of(TimeZone.getDefault().inDaylightTime(new Date()) ? "+2" : "+1")),
								flight.getActualStartTime(), flight.getActualLandingTime());
					}).collect(Collectors.toList());

			planeSchedules.add(new PlaneScheduleDto(plane.getId(), plane.getTypeData().getType().getName(),
					plane.getState().getName(), hasConflict.get(), flights));
		});

		conflicts.clear();
		ConflictFinder.findConflicts(planeSchedules, conflicts);

		return planeSchedules;
	}

	public ArrayList<ConflictDto> getConflicts() {
		return conflicts;
	}
}
