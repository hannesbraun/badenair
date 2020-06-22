package de.hso.badenair.service.flight;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import de.hso.badenair.controller.dto.flight.FlightDto;
import de.hso.badenair.controller.dto.flight.TrackingDto;
import de.hso.badenair.domain.flight.Flight;
import de.hso.badenair.domain.flight.FlightAction;
import de.hso.badenair.domain.flight.FlightCrewMember;
import de.hso.badenair.domain.flight.FlightState;
import de.hso.badenair.domain.plane.PlaneState;
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

		final OffsetDateTime currentTime = OffsetDateTime.now().withOffsetSameLocal(ZoneOffset.of("+1"));

		if (!flight.isPresent()) {
			return null;
		}

		if (dto.getAction().equals(FlightAction.START.getName())) {
			flight.get().setActualStartTime(currentTime);
			flight.get().setState(FlightState.OK);
			flight.get().getPlane().setState(PlaneState.ON_FLIGHT);
			flightRepository.save(flight.get());
		} else if (dto.getAction().equals(FlightAction.LANDING.getName())) {
			flight.get().setActualLandingTime(currentTime);

			// Add flight hours
			flight.get().getPlane().addFlightHours(
					Double.valueOf(Duration.between(flight.get().getActualStartTime(), currentTime).getSeconds())
							/ 3600.0);

			// Update state
			if (flight.get().getPlane().getFlightHours() > 2000.0) {
				// Cyclic maintenance
				flight.get().getPlane().setState(PlaneState.IN_MAINTENANCE);
			} else {
				flight.get().getPlane().setState(PlaneState.WAITING);
			}

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
			return new TrackingDto(FlightAction.DELAY.getName(), flight.getDelay(),
					OffsetDateTime.now().withOffsetSameLocal(ZoneOffset.of("+1")));
		}

		return new TrackingDto(FlightAction.STANDBY.getName(), flight.getDelay(),
				OffsetDateTime.now().withOffsetSameLocal(ZoneOffset.of("+1")));
	}

	public boolean setMaintenance(Long flightId) {
		Optional<Flight> flight = flightRepository.findById(flightId);

		if (!flight.isPresent()) {
			return false;
		}

		flight.get().getPlane().setState(PlaneState.IN_MAINTENANCE);
		flightRepository.save(flight.get());

		return true;
	}

	public FlightDto getCurrentFlightForPilot(String userName) {

		List<FlightCrewMember> members = flightCrewMemberRepository.findByEmployeeUserId(userName);
		List<Flight> flights = new ArrayList<>();

		for (FlightCrewMember member : members) {
			flights.add(member.getFlight());
		}

		// Filter out flights with plane in maintenance
		flights = flights.stream().filter(flight -> flight.getPlane().getState() != PlaneState.IN_MAINTENANCE
				|| flight.getActualStartTime() != null).collect(Collectors.toList());

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
						"UTC+1", "UTC+1", 0.0);
			} else if (DateFusioner
					.fusionStartDate(flight.getStartDate(), flight.getScheduledFlight().getStartTime(), null)
					.isAfter(OffsetDateTime.now().withOffsetSameLocal(ZoneOffset.of("+1")))
					&& flight.getActualStartTime() == null) {
				// Flight is in the future
				return new FlightDto(flight.getId(), flight.getScheduledFlight().getStartingAirport().getName(),
						flight.getScheduledFlight().getDestinationAirport().getName(),
						DateFusioner.fusionStartDate(flight.getStartDate(), flight.getScheduledFlight().getStartTime(),
								null),
						DateFusioner.fusionArrivalDate(flight.getStartDate(),
								flight.getScheduledFlight().getStartTime(),
								flight.getScheduledFlight().getDurationInHours(), null),
						"UTC+1", "UTC+1", 0.0);
			}
		}

		return null;
	}

	public List<FlightDto> getNextFlights(String userName) {
		List<FlightDto> nextFlights = new ArrayList<>();

		List<Flight> flightsRaw = flightCrewMemberRepository.findByEmployeeUserId(userName).stream()
				.map(assignment -> assignment.getFlight()).collect(Collectors.toList());

		flightsRaw.sort(Comparator.comparing(flight -> DateFusioner.fusionStartDate(flight.getStartDate(),
				flight.getScheduledFlight().getStartTime(), null)));

		for (Flight flight : flightsRaw) {
			if (flight.getActualStartTime() == null && flight.getActualLandingTime() == null
					&& DateFusioner
							.fusionStartDate(flight.getStartDate(), flight.getScheduledFlight().getStartTime(), null)
							.isAfter(OffsetDateTime.now().withOffsetSameLocal(ZoneOffset.of("+1")))) {
				// Flight is in the future
				nextFlights.add(new FlightDto(flight.getId(),
						flight.getScheduledFlight().getStartingAirport().getName(),
						flight.getScheduledFlight().getDestinationAirport().getName(),
						DateFusioner.fusionStartDate(flight.getStartDate(), flight.getScheduledFlight().getStartTime(),
								null),
						DateFusioner.fusionArrivalDate(flight.getStartDate(),
								flight.getScheduledFlight().getStartTime(),
								flight.getScheduledFlight().getDurationInHours(), null),
						"UTC+1", "UTC+1", 0.0));
			}
		}

		return nextFlights;
	}

	public Flight getFlightById(Long id) {
		Optional<Flight> flight = flightRepository.findById(id);

		if (!flight.isPresent()) {
			return null;
		}

		return flight.get();
	}
}
