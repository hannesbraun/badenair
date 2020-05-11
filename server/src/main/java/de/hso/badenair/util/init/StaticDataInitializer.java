package de.hso.badenair.util.init;

import java.io.FileNotFoundException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import de.hso.badenair.domain.flight.Airport;
import de.hso.badenair.domain.flight.Flight;
import de.hso.badenair.domain.flight.FlightState;
import de.hso.badenair.domain.flight.ScheduledFlight;
import de.hso.badenair.domain.plane.Plane;
import de.hso.badenair.domain.plane.PlaneState;
import de.hso.badenair.domain.plane.PlaneType;
import de.hso.badenair.domain.plane.PlaneTypeData;
import de.hso.badenair.service.airport.AirportRepository;
import de.hso.badenair.service.flight.repository.FlightRepository;
import de.hso.badenair.service.flight.repository.ScheduledFlightRepository;
import de.hso.badenair.service.plane.repository.PlaneRepository;
import de.hso.badenair.service.plane.repository.PlaneTypeDataRepository;
import de.hso.badenair.util.csv.CsvHelper;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StaticDataInitializer {

	private final PlaneTypeDataRepository planeTypeDataRepository;

	private final PlaneRepository planeRepository;

	private final AirportRepository airportRepository;

	private final ScheduledFlightRepository scheduledFlightRepository;

	private final FlightRepository flightRepository;

	private final boolean demoMode = false;

	@PostConstruct
	private void init() {
		initPlaneTypeData();
		initPlanes();

		initAirports();

		initFlightplan();
		// generateFlightplan();
	}

	private void initPlaneTypeData() {
		final PlaneTypeData planeTypeDataDash_8_400 = PlaneTypeData.builder()
				.type(PlaneType.Dash_8_400).numberOfPassengers(68)
				.flightRange(1000).build();

		final PlaneTypeData planeTypeDataDash_8_200 = PlaneTypeData.builder()
				.type(PlaneType.Dash_8_200).numberOfPassengers(38)
				.flightRange(1000).build();

		final PlaneTypeData planeTypeDataB737_400 = PlaneTypeData.builder()
				.type(PlaneType.B737_400).numberOfPassengers(188)
				.flightRange(2500).build();

		planeTypeDataRepository.saveAll(List.of(planeTypeDataDash_8_400,
				planeTypeDataDash_8_200, planeTypeDataB737_400));
	}

	private void initPlanes() {
		// Get types
		PlaneTypeData planeTypeDataDash_8_200 = planeTypeDataRepository
				.findAllByType(PlaneType.Dash_8_200).get(0);
		PlaneTypeData planeTypeDataDash_8_400 = planeTypeDataRepository
				.findAllByType(PlaneType.Dash_8_400).get(0);
		PlaneTypeData planeTypeDataB737_400 = planeTypeDataRepository
				.findAllByType(PlaneType.B737_400).get(0);

		List<Plane> planes = new ArrayList<Plane>();

		// Number of available plane
		final int dash_8_200_count = 3;
		final int dash_8_400_count = 5;
		final int b737_400_count = 2;

		// Create planes
		for (int i = 0; i < dash_8_200_count; i++) {
			planes.add(Plane.builder().typeData(planeTypeDataDash_8_200)
					.state(PlaneState.WAITING).traveledDistance(0).build());
		}

		for (int i = 0; i < dash_8_400_count; i++) {
			planes.add(Plane.builder().typeData(planeTypeDataDash_8_400)
					.state(PlaneState.WAITING).traveledDistance(0).build());
		}

		for (int i = 0; i < b737_400_count; i++) {
			planes.add(Plane.builder().typeData(planeTypeDataB737_400)
					.state(PlaneState.WAITING).traveledDistance(0).build());
		}

		planeRepository.saveAll(planes);
	}

	private void initAirports() {
		List<Airport> airports = new ArrayList<Airport>();

		try {
			List<String[]> data = CsvHelper.getData(
					ResourceUtils.getFile("classpath:data/airports.csv"));

			// Get all (valid) airports
			for (String[] airportData : data) {
				if (airportData.length == 3) {
					airports.add(Airport.builder().name(airportData[0])
							.distance(Integer.valueOf(airportData[1]))
							.timezone(airportData[2]).build());
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		airportRepository.saveAll(airports);
	}

	private void initFlightplan() {
		// Get planes
		List<Plane> initialPlanes = new LinkedList<Plane>();
		planeRepository.findAll().forEach(initialPlanes::add);
		HashMap<String, Plane> mappedPlanes = new HashMap<String, Plane>();

		// Demo mode: adjusted flightplan
		String flightplanFile;
		if (!demoMode) {
			flightplanFile = "flightplan.csv";
		} else {
			flightplanFile = "flightplanDemo.csv";
		}

		try {
			List<String[]> data = CsvHelper.getData(
					ResourceUtils.getFile("classpath:data/" + flightplanFile));

			// Map ids of planes (in case they change)
			for (String[] flightData : data) {
				if (flightData.length == 8) {
					if (mappedPlanes.containsKey(flightData[7])) {
						// Id already mapped
						continue;
					}

					// Find unmapped plane
					for (Plane plane : initialPlanes) {
						if (plane.getTypeData().getType().getName()
								.equals(flightData[6])) {
							mappedPlanes.put(flightData[7], plane);
						}
					}
					initialPlanes.remove(flightData[7]);
				}
			}

			// Dates stored for consistency
			OffsetDateTime now = OffsetDateTime.now()
					.withOffsetSameLocal(ZoneOffset.of("+1"));;
			OffsetDateTime endOfPlanDate = now.plusMonths(12);

			// Get all (valid) scheduled flights
			for (String[] flightData : data) {
				if (flightData.length == 8) {
					int hour = Integer.valueOf(flightData[4].substring(0,
							flightData[4].indexOf(":")));
					int minute = Integer.valueOf(flightData[4]
							.substring(flightData[4].indexOf(":") + 1));

					ScheduledFlight scheduledFlight = ScheduledFlight.builder()
							.startingAirport(airportRepository
									.findByName(flightData[0]).get())
							.destinationAirport(airportRepository
									.findByName(flightData[1]).get())
							.basePrice(Double.valueOf(flightData[2]))
							.durationInHours(Double.valueOf(flightData[3]))
							.startTime(now.withHour(hour).withMinute(minute))
							.build();
					scheduledFlightRepository.save(scheduledFlight);

					Plane plane = mappedPlanes.get(flightData[7]);

					OffsetDateTime startDate = now
							.plusDays(Math.floorMod(
									Integer.valueOf(flightData[5])
											- now.getDayOfWeek().getValue(),
									7));
					do {
						// Add flights for the next 12 months
						flightRepository.save(Flight.builder()
								.scheduledFlight(scheduledFlight)
								.state(FlightState.OK).plane(plane)
								.startDate(startDate).build());
						startDate.plusDays(7l);
					} while (startDate.isBefore(endOfPlanDate));

				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	// Attention: ignore the following code. Your eyes may bleed.

	// private void generateFlightplan() {
	// // Constants
	// final double planeSpeedMph = 2500 / 4.0;
	// final double pricePerMile = 29.99 / 660;
	// final int pause;
	// if (!demoMode) {
	// pause = 15;
	// } else {
	// pause = 2;
	// }
	//
	// List<ScheduledFlight> scheduledFlights = new
	// ArrayList<ScheduledFlight>();
	// List<Flight> flights = new ArrayList<Flight>();
	//
	// // Get airports
	// List<Airport> airports = new LinkedList<Airport>();
	// airportRepository.findAll().forEach(airports::add);
	// Airport home = null;
	// for (Airport airport : airports) {
	// if (airport.getName().equals("Karlsruhe/Baden-Baden")) {
	// home = airport;
	// break;
	// }
	// }
	// airports.remove(home);
	//
	// // Add scheduled flights (without time)
	// // Note: one scheduled flight to exact one flight per week
	// for (Airport airport : airports) {
	// double duration;
	// if (!demoMode) {
	// duration = Double.valueOf(airport.getDistance())
	// / planeSpeedMph;
	// } else {
	// duration = 0.05;
	// }
	// double basePrice = airport.getDistance() * pricePerMile;
	// int repeatFlight = 1;
	// if (airport.getDistance() < 1000.0) {
	// if (!demoMode) {
	// repeatFlight = 5;
	// } else {
	// repeatFlight = 100;
	// }
	// } else if (demoMode) {
	// repeatFlight = 35;
	// }
	//
	// for (int i = 0; i < repeatFlight; i++) {
	// scheduledFlights.add(ScheduledFlight
	// .builder().startingAirport(home)
	// .destinationAirport(airport).durationInHours(duration)
	// .basePrice(basePrice).startTime(OffsetDateTime.of(1756,
	// 1, 27, 0, 0, 0, 0, ZoneOffset.of("+1")))
	// .build());
	// }
	//
	// }
	// Collections.shuffle(scheduledFlights, new Random(421943l));
	//
	// // Get planes
	// List<Plane> planes = new LinkedList<Plane>();
	// planeRepository.findAll().forEach(planes::add);
	// boolean dash_8_200_removed = false;
	// boolean dash_8_400_removed = false;
	// List<Plane> dashPlanes = new LinkedList<Plane>();
	// List<Plane> boeingPlanes = new LinkedList<Plane>();
	// for (Plane plane : planes) {
	// if (plane.getTypeData().getType() == PlaneType.Dash_8_200
	// && !dash_8_200_removed) {
	// dash_8_200_removed = true;
	// } else if (plane.getTypeData().getType() == PlaneType.Dash_8_400
	// && !dash_8_400_removed) {
	// dash_8_400_removed = true;
	// } else if (plane.getTypeData().getType() == PlaneType.B737_400) {
	// boeingPlanes.add(plane);
	// } else {
	// dashPlanes.add(plane);
	// }
	// }
	//
	// List<OffsetDateTime> departureTimes = new ArrayList<OffsetDateTime>();
	//
	// OffsetDateTime earliestStartTime = OffsetDateTime.of(1849, 3, 14, 6, 0,
	// 0, 0, ZoneOffset.of("+1"));
	// OffsetDateTime tmpDate = earliestStartTime;
	// OffsetDateTime now = OffsetDateTime.now()
	// .withOffsetSameLocal(ZoneOffset.of("+1"));
	// OffsetDateTime tmpDateWeek = now;
	// double dailyFlightTime = 0.0;
	// Iterator<Plane> boeingIterator = boeingPlanes.iterator();
	// Plane boeing = boeingIterator.next();
	// for (ScheduledFlight scheduledFlight : scheduledFlights) {
	// if (scheduledFlight.getDestinationAirport()
	// .getDistance() > 1000.0) {
	// scheduledFlight.setStartTime(tmpDate);
	// flights.add(Flight.builder().plane(boeing)
	// .scheduledFlight(scheduledFlight).state(FlightState.OK)
	// .startDate(tmpDateWeek).build());
	// departureTimes.add(tmpDateWeek.withHour(tmpDate.getHour())
	// .withMinute(tmpDate.getMinute()));
	//
	// tmpDate = getNextPossibleDeparture(tmpDateWeek,
	// tmpDate.plusMinutes(
	// (long) (scheduledFlight.getDurationInHours()
	// * 60.0 * 2.0) + 2 * pause),
	// departureTimes);
	// dailyFlightTime += scheduledFlight.getDurationInHours() * 2.0;
	// if (dailyFlightTime > 7.0) {
	// // 7hr per day per plane
	// dailyFlightTime = 0.0;
	// tmpDate = earliestStartTime;
	// if (tmpDateWeek.plusDays(1l)
	// .isBefore(now.minusSeconds(1l).plusDays(7l))) {
	// tmpDateWeek = tmpDateWeek.plusDays(1l);
	// } else {
	// tmpDateWeek = now;
	// if (boeingIterator.hasNext()) {
	// boeing = boeingIterator.next();
	// } else {
	// break;
	// }
	// }
	//
	// tmpDate = getNextPossibleDeparture(tmpDateWeek,
	// earliestStartTime, departureTimes);
	// }
	// }
	// }
	//
	// Iterator<Plane> dashIterator = dashPlanes.iterator();
	// Plane dash = dashIterator.next();
	// boolean nextDay = false;
	// tmpDateWeek = now;
	// tmpDate = getNextPossibleDeparture(tmpDateWeek, earliestStartTime,
	// departureTimes);
	// for (ScheduledFlight scheduledFlight : scheduledFlights) {
	// if (scheduledFlight.getDestinationAirport()
	// .getDistance() < 1000.0) {
	// scheduledFlight.setStartTime(tmpDate);
	// flights.add(Flight.builder().plane(dash)
	// .scheduledFlight(scheduledFlight).state(FlightState.OK)
	// .startDate(tmpDateWeek).build());
	// tmpDate = getNextPossibleDeparture(tmpDateWeek,
	// tmpDate.plusMinutes(
	// (long) (scheduledFlight.getDurationInHours()
	// * 60.0 * 2.0) + 2 * pause),
	// departureTimes);
	// if (tmpDate.getHour() >= 0 && tmpDate.getHour() < 6
	// && !nextDay) {
	// tmpDateWeek = tmpDateWeek.plusDays(1l);
	// nextDay = true;
	// }
	// if ((!demoMode && tmpDate.getHour() > 0
	// && tmpDate.getHour() < 6)
	// || (demoMode && tmpDate.getHour() == 5
	// && tmpDate.getMinute() > 45)) {
	// // "Day" is over
	// if (!nextDay) {
	// tmpDateWeek = tmpDateWeek.plusDays(1l);
	// }
	// nextDay = false;
	// if (!tmpDateWeek
	// .isBefore(now.minusSeconds(1l).plusDays(7l))) {
	// tmpDateWeek = now;
	// if (dashIterator.hasNext()) {
	// dash = dashIterator.next();
	// } else {
	// break;
	// }
	// }
	//
	// tmpDate = getNextPossibleDeparture(tmpDateWeek,
	// earliestStartTime, departureTimes);
	// }
	// }
	// }
	//
	// // Check if all scheduled flights are processed
	// List<ScheduledFlight> notIncludedScheduledFlights = new ArrayList<>();
	// for (ScheduledFlight scheduledFlight : scheduledFlights) {
	// if (scheduledFlight.getStartTime().getYear() == 1756) {
	// System.err.println("Flight to "
	// + scheduledFlight.getDestinationAirport().getName()
	// + " not included in flightplan.");
	// notIncludedScheduledFlights.add(scheduledFlight);
	// }
	// }
	// scheduledFlights.removeAll(notIncludedScheduledFlights);
	//
	// // Generate flights back to Baden Baden
	// List<ScheduledFlight> scheduledReturnFlights = new
	// ArrayList<ScheduledFlight>();
	// List<Flight> returnFlights = new ArrayList<Flight>();
	// for (ScheduledFlight scheduledFlight : scheduledFlights) {
	// OffsetDateTime timeForReturnFlight = scheduledFlight.getStartTime()
	// .plusMinutes(
	// (long) (scheduledFlight.getDurationInHours() * 60.0)
	// + pause);
	// ScheduledFlight returnFlight = ScheduledFlight.builder()
	// .startingAirport(scheduledFlight.getDestinationAirport())
	// .destinationAirport(home)
	// .durationInHours(scheduledFlight.getDurationInHours())
	// .basePrice(scheduledFlight.getBasePrice())
	// .startTime(timeForReturnFlight).build();
	// scheduledReturnFlights.add(returnFlight);
	//
	// for (Flight flight : flights) {
	// // Search corresponding flight
	// OffsetDateTime returnFlightDate = flight.getStartDate();
	// if (scheduledFlight.getStartTime()
	// .getDayOfMonth() != timeForReturnFlight
	// .getDayOfMonth()) {
	// returnFlightDate = returnFlightDate.plusDays(1l);
	// }
	// if (flight.getScheduledFlight() == scheduledFlight) {
	// returnFlights.add(Flight.builder().plane(flight.getPlane())
	// .scheduledFlight(returnFlight).state(FlightState.OK)
	// .startDate(returnFlightDate).build());
	// break;
	// }
	// }
	// }
	// scheduledFlights.addAll(scheduledReturnFlights);
	// flights.addAll(returnFlights);
	//
	// // Write to file
	// StringBuilder csvOut = new StringBuilder();
	// for (Flight flight : flights) {
	// csvOut.append(
	// flight.getScheduledFlight().getStartingAirport().getName());
	// csvOut.append(',');
	// csvOut.append(flight.getScheduledFlight().getDestinationAirport()
	// .getName());
	// csvOut.append(',');
	// csvOut.append(flight.getScheduledFlight().getBasePrice());
	// csvOut.append(',');
	// csvOut.append(flight.getScheduledFlight().getDurationInHours());
	// csvOut.append(',');
	// csvOut.append(flight.getScheduledFlight().getStartTime().getHour());
	// csvOut.append(':');
	// csvOut.append(
	// flight.getScheduledFlight().getStartTime().getMinute());
	// csvOut.append(',');
	// csvOut.append(flight.getStartDate().getDayOfWeek().getValue());
	// csvOut.append(',');
	// csvOut.append(flight.getPlane().getTypeData().getType().getName());
	// csvOut.append(',');
	// csvOut.append(flight.getPlane().getId());
	// csvOut.append('\n');
	// }
	//
	// try {
	// // File outFile = ResourceUtils
	// // .getFile("classpath:data/flightplan.csv");
	// String fileName;
	// if (!demoMode) {
	// fileName = "flightplan.csv";
	// } else {
	// fileName = "flightplanDemo.csv";
	// }
	// BufferedWriter writer = new BufferedWriter(
	// new FileWriter("/Users/hannesbraun/" + fileName, false));
	// writer.append(csvOut.toString());
	// writer.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// private OffsetDateTime getNextPossibleDeparture(OffsetDateTime date,
	// OffsetDateTime time, List<OffsetDateTime> departureTimes) {
	// for (OffsetDateTime departureTime : departureTimes) {
	// for (long i = (demoMode ? 0 : -1); i < (demoMode ? 1 : 2); i++) {
	// if (date.withHour(time.getHour()).withMinute(time.getMinute())
	// .plusMinutes(i).isEqual(departureTime)) {
	// // Adjust and check
	// OffsetDateTime adjustedTime = time.plusMinutes(i + 2);
	// if (time.getDayOfMonth() != adjustedTime.getDayOfMonth()) {
	// date = date.plusDays(1l);
	// }
	// return getNextPossibleDeparture(date, adjustedTime,
	// departureTimes);
	// }
	// }
	// }
	//
	// // Okay, no adjustment
	// return time;
	// }
}
