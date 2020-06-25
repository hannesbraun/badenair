package de.hso.badenair.controller.flightplan;

import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.hso.badenair.controller.dto.flightplan.FlightWithoutPriceDto;
import de.hso.badenair.controller.dto.flightplan.PlaneScheduleDto;
import de.hso.badenair.service.flightplan.FlightPlanService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/employee/flightplan")
@RequiredArgsConstructor
public class FlightPlanController {
	private final FlightPlanService flightplanService;

	@GetMapping
	public ResponseEntity<List<PlaneScheduleDto>> getPlaneSchedules() {
		List<PlaneScheduleDto> planeSchedules = flightplanService.getPlaneSchedules();

		// Fix dates
		planeSchedules.stream().forEach(planeSchedule -> {
			planeSchedule.setFlights(planeSchedule.getFlights().stream().map(flight -> {
				return new FlightWithoutPriceDto(flight.getId(), flight.getStart(), flight.getDestination(),
						flight.getDelay(),
						flight.getStartTime().withOffsetSameLocal(
								ZoneOffset.of(TimeZone.getDefault().inDaylightTime(new Date()) ? "+2" : "+1")),
						flight.getArrivalTime().withOffsetSameLocal(
								ZoneOffset.of(TimeZone.getDefault().inDaylightTime(new Date()) ? "+2" : "+1")),
						flight.getRealStartTime() != null
								? flight.getRealStartTime().withOffsetSameLocal(
										ZoneOffset.of(TimeZone.getDefault().inDaylightTime(new Date()) ? "+2" : "+1"))
								: null,
						flight.getRealStartTime() != null
								? flight.getRealLandingTime().withOffsetSameLocal(
										ZoneOffset.of(TimeZone.getDefault().inDaylightTime(new Date()) ? "+2" : "+1"))
								: null);
			}).collect(Collectors.toList()));
		});

		return ResponseEntity.ok(planeSchedules);
	}
}
