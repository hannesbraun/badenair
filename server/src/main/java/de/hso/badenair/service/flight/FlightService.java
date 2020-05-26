package de.hso.badenair.service.flight;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import de.hso.badenair.controller.dto.flight.FlightDto;
import de.hso.badenair.controller.dto.flight.TrackingDto;
import de.hso.badenair.domain.flight.Flight;
import de.hso.badenair.domain.flight.FlightAction;
import de.hso.badenair.domain.flight.FlightCrewMember;
import de.hso.badenair.domain.flight.FlightState;
import de.hso.badenair.service.flight.repository.FlightCrewMemberRepository;
import de.hso.badenair.service.flight.repository.FlightRepository;
import de.hso.badenair.service.flight.repository.ScheduledFlightRepository;
import de.hso.badenair.service.keycloakapi.KeycloakApiService;
import de.hso.badenair.util.time.DateFusioner;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FlightService {

	private final FlightRepository flightRepository;
	private final FlightCrewMemberRepository flightCrewMemberRepository;
	private final ScheduledFlightRepository scheduledFlightRepository;
	private final KeycloakApiService keycloakApiService;

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
			// TODO: SET Duration of Delay
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
			// TODO: return dto with actual delay from db
			return new TrackingDto(FlightAction.START.getName(), 0L, flight.getActualStartTime());
		} else if (flight.getActualStartTime() != null && flight.getActualLandingTime() != null) {
			// TODO: return dto with actual delay from db
			return new TrackingDto(FlightAction.LANDING.getName(), 0L, flight.getActualLandingTime());
		} else if (flight.getActualStartTime() == null && flight.getActualLandingTime() == null
				&& flight.getState() == FlightState.DELAYED) {
			// TODO: return dto with actual delay from db
			return new TrackingDto(FlightAction.DELAY.getName(), 0L, OffsetDateTime.now());
		}
		// TODO: return dto with actual delay from db;
		return new TrackingDto(FlightAction.STANDBY.getName(), 0L, OffsetDateTime.now());
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
				return new FlightDto(flight.getId(), flight.getScheduledFlight().getStartingAirport().getName(),
						flight.getScheduledFlight().getDestinationAirport().getName(), flight.getActualStartTime(),
						flight.getActualLandingTime(), 0);
			} else if (DateFusioner
					.fusionStartDate(flight.getStartDate(), flight.getScheduledFlight().getStartTime(), null)
					.isAfter(OffsetDateTime.now())) {
				return new FlightDto(flight.getId(), flight.getScheduledFlight().getStartingAirport().getName(),
						flight.getScheduledFlight().getDestinationAirport().getName(), flight.getActualStartTime(),
						flight.getActualLandingTime(), 0);
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
