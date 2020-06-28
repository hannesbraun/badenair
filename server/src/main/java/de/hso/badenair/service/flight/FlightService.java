package de.hso.badenair.service.flight;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.hso.badenair.controller.dto.flight.FlightDto;
import de.hso.badenair.controller.dto.flight.TrackingDto;
import de.hso.badenair.domain.flight.Flight;
import de.hso.badenair.domain.flight.FlightAction;
import de.hso.badenair.domain.flight.FlightCrewMember;
import de.hso.badenair.domain.flight.FlightState;
import de.hso.badenair.domain.plane.Plane;
import de.hso.badenair.domain.plane.PlaneState;
import de.hso.badenair.service.flight.repository.FlightCrewMemberRepository;
import de.hso.badenair.service.flight.repository.FlightRepository;
import de.hso.badenair.service.keycloakapi.KeycloakApiService;
import de.hso.badenair.util.time.DateFusioner;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FlightService {

	private final FlightRepository flightRepository;
	private final KeycloakApiService keycloakApiService;
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
					Duration.between(flight.get().getActualStartTime(), currentTime).getSeconds() / 3600.0);

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

			OffsetDateTime currentFlightDateTime = DateFusioner.fusionStartDate(flight.get().getStartDate(),
					flight.get().getScheduledFlight().getStartTime(), null);
			OffsetDateTime nextWorkingDay = calculateNextWorkingDay(currentFlightDateTime);
			cascadeDelay(currentFlightDateTime, nextWorkingDay, flight.get(), dto.getDelay());
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

		int currentMonth = OffsetDateTime.now().getMonthValue();
		int nextMonth = (currentMonth % 12) + 1;
		int currentMonthYear = OffsetDateTime.now().getYear();
		int nextMonthYear = currentMonth < 12 ? currentMonthYear : currentMonthYear + 1;

		if (OffsetDateTime.now().getDayOfMonth() < 10) {
			// Show plan for current
			return nextFlights.stream().filter(flight -> flight.getStartTime().getMonthValue() == currentMonth
					&& flight.getStartTime().getYear() == currentMonthYear).collect(Collectors.toList());
		} else {
			// Also show plan for next month
			return nextFlights.stream()
					.filter(flight -> (flight.getStartTime().getMonthValue() == currentMonth
							|| flight.getStartTime().getMonthValue() == nextMonth)
							&& (flight.getStartTime().getYear() == currentMonthYear
									|| flight.getStartTime().getYear() == nextMonthYear))
					.collect(Collectors.toList());
		}
	}

	public Flight getFlightById(Long id) {
		Optional<Flight> flight = flightRepository.findById(id);

		if (!flight.isPresent()) {
			return null;
		}

		return flight.get();
	}

	private OffsetDateTime calculateNextWorkingDay(OffsetDateTime currentFlightDateTime) {
		OffsetDateTime nextWorkingDay = currentFlightDateTime.plusDays(1).withHour(6).withMinute(0).withSecond(0)
				.withNano(0);
		if (currentFlightDateTime.isBefore(currentFlightDateTime.withHour(6).withMinute(0).withSecond(0).withNano(0))) {
			nextWorkingDay = currentFlightDateTime.withHour(6).withMinute(0).withSecond(0).withNano(0);
		}

		return nextWorkingDay;
	}

	private List<Flight> getPlaneFlightsForCurrentAndNextDay(Plane plane, OffsetDateTime currentFlightDateTime,
			OffsetDateTime nextWorkingDay) {
		OffsetDateTime start = currentFlightDateTime.withHour(0).withMinute(0).withSecond(0).withNano(0);
		OffsetDateTime end = nextWorkingDay.withHour(23).withMinute(59).withSecond(59).withNano(999999);

		return flightRepository.findByPlaneAndStartDateBetween(plane, start, end);
	}

	private List<Flight> filterPendingFlightsForCurrentWorkday(List<Flight> flights,
			OffsetDateTime currentFlightDateTime, OffsetDateTime nextWorkingDay) {
		List<Flight> returnList = new ArrayList<>();
		for (Flight tempFlight : flights) {
			if (DateFusioner
					.fusionStartDate(tempFlight.getStartDate(), tempFlight.getScheduledFlight().getStartTime(), null)
					.isAfter(currentFlightDateTime)
					&& DateFusioner.fusionStartDate(tempFlight.getStartDate(),
							tempFlight.getScheduledFlight().getStartTime(), null).isBefore(nextWorkingDay)) {
				returnList.add(tempFlight);
			}
		}

		returnList.sort(Comparator.comparing(tempFlight -> DateFusioner.fusionStartDate(tempFlight.getStartDate(),
				tempFlight.getScheduledFlight().getStartTime(), null)));

		return returnList;
	}

	@Transactional
	public boolean cascadeDelay(OffsetDateTime currentFlightDateTime, OffsetDateTime nextWorkingDay,
			Flight alreadyDelayedFlight, double delay) {

		double leftoverDelayForPlane = delay;
		double leftoverDelayForPilot = delay;

		// delay over 15 minutes
		if (leftoverDelayForPlane > 15) {
			// get pending Flights with current Plane
			List<Flight> tempFlights = getPlaneFlightsForCurrentAndNextDay(alreadyDelayedFlight.getPlane(),
					currentFlightDateTime, nextWorkingDay);
			List<Flight> currentDayFlights = filterPendingFlightsForCurrentWorkday(tempFlights, currentFlightDateTime,
					nextWorkingDay);

			Instant i1;
			// cascade delay on all flights on this plane for the current workday
			for (int i = 0; i < currentDayFlights.size(); i++) {
				if (i - 1 >= 0) {
					i1 = DateFusioner
							.fusionArrivalDate(currentDayFlights.get(i - 1).getStartDate(),
									currentDayFlights.get(i - 1).getScheduledFlight().getStartTime(),
									currentDayFlights.get(i - 1).getScheduledFlight().getDurationInHours(), null)
							.toInstant();
				} else {
					i1 = DateFusioner.fusionArrivalDate(alreadyDelayedFlight.getStartDate(),
							alreadyDelayedFlight.getScheduledFlight().getStartTime(),
							alreadyDelayedFlight.getScheduledFlight().getDurationInHours(), null).toInstant();
				}
				Instant i2 = DateFusioner.fusionStartDate(currentDayFlights.get(i).getStartDate(),
						currentDayFlights.get(i).getScheduledFlight().getStartTime(), null).toInstant();
				double bufferMinutes = Duration.between(i1, i2).getSeconds() / 60;
				if ((leftoverDelayForPlane - bufferMinutes) > 0) {
					leftoverDelayForPlane -= bufferMinutes;

					currentDayFlights.get(i).setState(FlightState.DELAYED);
					currentDayFlights.get(i).setDelay(leftoverDelayForPlane);
					OffsetDateTime delayedFlightDateTime = DateFusioner.fusionStartDate(
							currentDayFlights.get(i).getStartDate(),
							currentDayFlights.get(i).getScheduledFlight().getStartTime(), null);
					if (!cascadeDelay(delayedFlightDateTime, nextWorkingDay, currentDayFlights.get(i),
							leftoverDelayForPlane)) {
						return false;
					}

				}
			}
		}

		if (leftoverDelayForPilot > 60) {
			List<FlightCrewMember> flightCrew = flightCrewMemberRepository.findByFlight(alreadyDelayedFlight);
			FlightCrewMember pilot = null;
			List<Flight> currentDayPilotFlights;
			if (flightCrew.size() > 0) {
				pilot = flightCrew.get(0);
			}

			if (pilot != null) {
				List<FlightCrewMember> memberEntries = flightCrewMemberRepository
						.findByEmployeeUserId(pilot.getEmployeeUserId());
				List<Flight> pilotTempFlights = new ArrayList<>();
				for (FlightCrewMember memberEntry : memberEntries) {
					pilotTempFlights.add(memberEntry.getFlight());
				}
				currentDayPilotFlights = filterPendingFlightsForCurrentWorkday(pilotTempFlights, currentFlightDateTime,
						nextWorkingDay);

				for (FlightCrewMember memberEntry : memberEntries) {
					pilotTempFlights.add(memberEntry.getFlight());
				}

				Instant i1;
				for (int i = 0; i < currentDayPilotFlights.size(); i++) {
					if (i - 1 >= 0) {
						i1 = DateFusioner.fusionArrivalDate(currentDayPilotFlights.get(i - 1).getStartDate(),
								currentDayPilotFlights.get(i - 1).getScheduledFlight().getStartTime(),
								currentDayPilotFlights.get(i - 1).getScheduledFlight().getDurationInHours(), null)
								.toInstant();
					} else {
						i1 = DateFusioner
								.fusionArrivalDate(alreadyDelayedFlight.getStartDate(),
										alreadyDelayedFlight.getScheduledFlight().getStartTime(),
										alreadyDelayedFlight.getScheduledFlight().getDurationInHours(), null)
								.toInstant();
					}
					Instant i2 = DateFusioner
							.fusionStartDate(currentDayPilotFlights.get(i).getStartDate(),
									currentDayPilotFlights.get(i).getScheduledFlight().getStartTime(), null)
							.toInstant();

					double bufferMinutes = Duration.between(i1, i2).getSeconds() / 60;
					if ((leftoverDelayForPilot - bufferMinutes) > 0) {
						leftoverDelayForPilot -= bufferMinutes;

						currentDayPilotFlights.get(i).setState(FlightState.DELAYED);
						currentDayPilotFlights.get(i).setDelay(leftoverDelayForPilot);
						OffsetDateTime delayedFlightDateTime = DateFusioner.fusionStartDate(
								currentDayPilotFlights.get(i).getStartDate(),
								currentDayPilotFlights.get(i).getScheduledFlight().getStartTime(), null);
						if (!cascadeDelay(delayedFlightDateTime, nextWorkingDay, currentDayPilotFlights.get(i),
								leftoverDelayForPilot)) {
							return false;
						}

					}
				}
			}
		}
		System.out.println("test");
		return true;
	}
}
