package de.hso.badenair.service.flight;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.hso.badenair.domain.flight.Flight;
import de.hso.badenair.domain.flight.FlightCrewMember;
import de.hso.badenair.domain.flight.FlightState;
import de.hso.badenair.domain.plane.Plane;
import de.hso.badenair.service.flight.repository.FlightCrewMemberRepository;
import de.hso.badenair.service.flight.repository.FlightRepository;
import de.hso.badenair.util.time.DateFusioner;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FlightCascade {
	private final FlightRepository flightRepository;

	private final FlightCrewMemberRepository flightCrewMemberRepository;

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
				double bufferMinutes = Duration.between(i1, i2).abs().getSeconds() / 60.0;
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
				} else {
					break;
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

					double bufferMinutes = Duration.between(i1, i2).abs().getSeconds() / 60.0;
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

					} else {
						break;
					}
				}
			}
		}

		return true;
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
}
