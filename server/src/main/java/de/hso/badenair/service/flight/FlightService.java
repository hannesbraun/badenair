package de.hso.badenair.service.flight;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import org.springframework.stereotype.Service;

import de.hso.badenair.controller.dto.flight.FlightDto;
import de.hso.badenair.controller.dto.flight.TrackingDto;
import de.hso.badenair.domain.flight.Flight;
import de.hso.badenair.domain.flight.FlightAction;
import de.hso.badenair.domain.flight.FlightCrewMember;
import de.hso.badenair.domain.flight.FlightState;
import de.hso.badenair.service.flight.repository.FlightCrewMemberRepository;
import de.hso.badenair.service.flight.repository.FlightRepository;
import de.hso.badenair.util.time.DateFusioner;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FlightService {

	private final FlightRepository flightRepository;

	private final FlightCrewMemberRepository flightCrewMemberRepository;

	public OffsetDateTime updateFlightTracking(Long flightId, TrackingDto dto) {
		Optional<Flight> flight = flightRepository.findById(flightId);

		final OffsetDateTime currentTime = OffsetDateTime.now();

		if (!flight.isPresent()) {
			return null;
		}

		if (dto.getAction().equals(FlightAction.START.getName())) {
			flight.get().setActualStartTime(currentTime);
			flight.get().setState(FlightState.OK);
			flightRepository.save(flight.get());
		} else if (dto.getAction().equals(FlightAction.LANDING.getName())) {
			flight.get().setActualLandingTime(currentTime);
			flightRepository.save(flight.get());
		} else if (dto.getAction().equals(FlightAction.DELAY.getName())) {
			flight.get().setState(FlightState.DELAYED);
			flight.get().setDelay(dto.getDelay());
			flightRepository.save(flight.get());
		} else {
			return null;
		}

		return currentTime;
	}

	public TrackingDto getFlightAction(Long flightId) {
		Flight flight = getFlightById(flightId);

		if (flight == null) {
			return null;
		}

		if (flight.getActualStartTime() != null && flight.getActualLandingTime() == null) {
			return new TrackingDto(FlightAction.START.getName(), flight.getDelay(), flight.getActualStartTime());
		} else if (flight.getActualStartTime() != null && flight.getActualLandingTime() != null) {
			return new TrackingDto(FlightAction.LANDING.getName(), flight.getDelay(), flight.getActualLandingTime());
		} else if (flight.getActualStartTime() == null && flight.getActualLandingTime() == null
				&& flight.getState() == FlightState.DELAYED) {
			return new TrackingDto(FlightAction.DELAY.getName(), flight.getDelay(), OffsetDateTime.now());
		}

		return new TrackingDto(FlightAction.STANDBY.getName(), flight.getDelay(), OffsetDateTime.now());
	}

	public FlightDto getCurrentFlightForPilot(String userName) {

		List<FlightCrewMember> members = flightCrewMemberRepository.findByEmployeeUserId(userName);
		List<Flight> flights = new ArrayList<>();

		for (FlightCrewMember member : members) {
			flights.add(member.getFlight());
		}

		flights.sort(Comparator.comparing(flight -> DateFusioner.fusionStartDate(flight.getStartDate(),
				flight.getScheduledFlight().getStartTime(), null)));

		for (Flight flight : flights) {
			if (flight.getActualStartTime() != null && flight.getActualLandingTime() == null) {
				// Plane is in the air
				return new FlightDto(flight.getId(), flight.getScheduledFlight().getStartingAirport().getName(),
						flight.getScheduledFlight().getDestinationAirport().getName(), flight.getActualStartTime(),
						DateFusioner.fusionArrivalDate(flight.getStartDate(),
								flight.getScheduledFlight().getStartTime(),
								flight.getScheduledFlight().getDurationInHours(), null),
						"UTC" + flight.getScheduledFlight().getStartingAirport().getTimezone(),
						"UTC" + flight.getScheduledFlight().getDestinationAirport().getTimezone(), 0.0);
			} else if (DateFusioner
					.fusionStartDate(flight.getStartDate(), flight.getScheduledFlight().getStartTime(), null)
					.isAfter(OffsetDateTime.now()) && flight.getActualStartTime() == null) {
				// Flight is in the future
				return new FlightDto(flight.getId(), flight.getScheduledFlight().getStartingAirport().getName(),
						flight.getScheduledFlight().getDestinationAirport().getName(),
						DateFusioner.fusionStartDate(flight.getStartDate(), flight.getScheduledFlight().getStartTime(),
								null),
						DateFusioner.fusionArrivalDate(flight.getStartDate(),
								flight.getScheduledFlight().getStartTime(),
								flight.getScheduledFlight().getDurationInHours(), null),
						"UTC" + (TimeZone.getDefault().inDaylightTime(new Date()) ? "+2" : "+1"),
						"UTC" + (TimeZone.getDefault().inDaylightTime(new Date()) ? "+2" : "+1"), 0.0);
			}
		}

		return null;
	}

	public Flight getFlightById(Long id) {
		Optional<Flight> flight = flightRepository.findById(id);

		if (!flight.isPresent()) {
			return null;
		}

		return flight.get();
	}
}
