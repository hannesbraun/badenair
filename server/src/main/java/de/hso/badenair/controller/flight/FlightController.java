package de.hso.badenair.controller.flight;

import java.security.Principal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.hso.badenair.controller.dto.flight.FlightDto;
import de.hso.badenair.controller.dto.flight.TrackingDto;
import de.hso.badenair.service.flight.FlightService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/employee/flight")
@RequiredArgsConstructor
public class FlightController {

	private final FlightService flightService;

	@PatchMapping("/tracking/{id}")
	public ResponseEntity<OffsetDateTime> updateFlightTracking(@PathVariable Long id, @RequestBody TrackingDto dto) {
		OffsetDateTime updateSuccess = flightService.updateFlightTracking(id, dto);

		if (updateSuccess != null) {
			updateSuccess = updateSuccess
					.withOffsetSameLocal(ZoneOffset.of(TimeZone.getDefault().inDaylightTime(new Date()) ? "+2" : "+1"));
			return ResponseEntity.ok(updateSuccess);
		} else {
			return new ResponseEntity<OffsetDateTime>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/tracking/action/{id}")
	public ResponseEntity<TrackingDto> getFlightAction(@PathVariable Long id) {
		TrackingDto resultDto = flightService.getFlightAction(id);

		if (resultDto != null) {
			resultDto = new TrackingDto(resultDto.getAction(), resultDto.getDelay(),
					resultDto.getDate().withOffsetSameLocal(
							ZoneOffset.of(TimeZone.getDefault().inDaylightTime(new Date()) ? "+2" : "+1")));
			return ResponseEntity.ok(resultDto);
		} else {
			return new ResponseEntity<TrackingDto>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/pilot")
	public ResponseEntity<FlightDto> getCurrentFlightForPilot(Principal user) {
		FlightDto result = flightService.getCurrentFlightForPilot(user.getName());

		// Fix timezone for corrct displaying in the frontend
		FlightDto resultFixed = null;
		if (result != null) {
			resultFixed = new FlightDto(result.getId(), result.getStart(), result.getDestination(),
					result.getStartTime().withOffsetSameLocal(
							ZoneOffset.of(TimeZone.getDefault().inDaylightTime(new Date()) ? "+2" : "+1")),
					result.getArrivalTime().withOffsetSameLocal(
							ZoneOffset.of(TimeZone.getDefault().inDaylightTime(new Date()) ? "+2" : "+1")),
					result.getStartTimezone(), result.getDestinationTimezone(), result.getPrice());
		}

		if (result != null) {
			return ResponseEntity.ok(resultFixed);
		} else {
			return new ResponseEntity<FlightDto>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/crewplan")
	public ResponseEntity<List<FlightDto>> getNextFlights(Principal user) {
		List<FlightDto> nextFlights = flightService.getNextFlights(user.getName());

		// Fix timezone for corrct displaying in the frontend
		List<FlightDto> nextFlightsFixed = new ArrayList<>();
		for (FlightDto flight : nextFlights) {
			nextFlightsFixed.add(new FlightDto(flight.getId(), flight.getStart(), flight.getDestination(),
					flight.getStartTime().withOffsetSameLocal(
							ZoneOffset.of(TimeZone.getDefault().inDaylightTime(new Date()) ? "+2" : "+1")),
					flight.getArrivalTime().withOffsetSameLocal(
							ZoneOffset.of(TimeZone.getDefault().inDaylightTime(new Date()) ? "+2" : "+1")),
					flight.getStartTimezone(), flight.getDestinationTimezone(), flight.getPrice()));
		}

		return ResponseEntity.ok(nextFlightsFixed);
	}
}
