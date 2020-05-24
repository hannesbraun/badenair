package de.hso.badenair.util.init;

import java.io.FileNotFoundException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import de.hso.badenair.controller.dto.flight.IncomingBookingDto;
import de.hso.badenair.controller.dto.plan.RequestVacationDto;
import de.hso.badenair.controller.dto.seat.SelectedSeatDto;
import de.hso.badenair.controller.dto.traveler.IncomingTravelerDto;
import de.hso.badenair.domain.flight.Airport;
import de.hso.badenair.domain.flight.Flight;
import de.hso.badenair.domain.flight.FlightState;
import de.hso.badenair.domain.flight.ScheduledFlight;
import de.hso.badenair.domain.plane.Plane;
import de.hso.badenair.domain.plane.PlaneState;
import de.hso.badenair.domain.plane.PlaneType;
import de.hso.badenair.domain.plane.PlaneTypeData;
import de.hso.badenair.service.airport.AirportRepository;
import de.hso.badenair.service.flight.booking.BookingService;
import de.hso.badenair.service.flight.repository.FlightRepository;
import de.hso.badenair.service.flight.repository.ScheduledFlightRepository;
import de.hso.badenair.service.keycloakapi.EmployeeRole;
import de.hso.badenair.service.keycloakapi.KeycloakApiService;
import de.hso.badenair.service.keycloakapi.dto.UserRepresentation;
import de.hso.badenair.service.plan.vacation.VacationService;
import de.hso.badenair.service.plane.repository.PlaneRepository;
import de.hso.badenair.service.plane.repository.PlaneTypeDataRepository;
import de.hso.badenair.util.csv.CsvHelper;
import lombok.RequiredArgsConstructor;

/**
 * Performs various data initialization tasks.
 */
@Component
@RequiredArgsConstructor
@DependsOn("keycloakApiService")
public class StaticDataInitializer {

	private final PlaneTypeDataRepository planeTypeDataRepository;

	private final PlaneRepository planeRepository;

	private final AirportRepository airportRepository;

	private final ScheduledFlightRepository scheduledFlightRepository;

	private final FlightRepository flightRepository;

	private final KeycloakApiService keycloakApiService;

	private final BookingService bookingService;

	private final VacationService vacationService;

	/**
	 * If set to true, demo mode will be enabled.
	 */
	private static final boolean DEMO_MODE = false;

	/**
	 * Defines the amount of customer accounts that will be created in the demo mode.
	 */
	private static final int AMOUNT_OF_CUSTOMERS = 1040;

	/**
	 * Defines the amount of bookings that will be generated for a customer in the demo mode.
	 */
	private static final int BOOKINGS_PER_CUSTOMER = 349;

	/**
	 * Inserts initial data into the database. If {@link DEMO_MODE} is set,
	 * additional demo data will be inserted.
	 */
	@PostConstruct
	private void init() {
		initPlaneTypeData();
		initPlanes();

		initAirports();

		initFlightplan();
		// generateFlightplan();
		
		initEmployees();
		
		if (DEMO_MODE) {
			initCustomers();
			initBookings();

			initVacation();
		}
	}

	/**
	 * Creates the available plane types in the database.
	 */
	private void initPlaneTypeData() {
		final PlaneTypeData planeTypeDataDash_8_400 = PlaneTypeData.builder()
				.type(PlaneType.Dash_8_400).numberOfPassengers(76)
				.numberOfRows(19).numberOfColumns(4).flightRange(1000).build();

		final PlaneTypeData planeTypeDataDash_8_200 = PlaneTypeData.builder()
				.type(PlaneType.Dash_8_200).numberOfPassengers(36)
				.numberOfRows(9).numberOfColumns(4).flightRange(1000).build();

		final PlaneTypeData planeTypeDataB737_400 = PlaneTypeData.builder()
				.type(PlaneType.B737_400).numberOfPassengers(186)
				.numberOfRows(31).numberOfColumns(6).flightRange(2500).build();

		planeTypeDataRepository.saveAll(List.of(planeTypeDataDash_8_400,
				planeTypeDataDash_8_200, planeTypeDataB737_400));
	}

	/**
	 * Creates the available planes in the database.
	 */
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

	/**
	 * Creates the available airports in the database.
	 */
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

	/**
	 * Initializes the flightplan data according to the file "flightplan.csv".
	 * If {@link DEMO_MODE} is set, a more dense flightplan will be used.
	 */
	private void initFlightplan() {
		// Get planes
		List<Plane> initialPlanes = new LinkedList<Plane>();
		planeRepository.findAll().forEach(initialPlanes::add);
		HashMap<String, Plane> mappedPlanes = new HashMap<String, Plane>();

		// Demo mode: adjusted flightplan
		String flightplanFile;
		if (!DEMO_MODE) {
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
					initialPlanes.remove(mappedPlanes.get(flightData[7]));
				}
			}

			// Dates stored for consistency
			OffsetDateTime now = OffsetDateTime.now()
					.withOffsetSameLocal(ZoneOffset.of("+1"));
			OffsetDateTime endOfPlanDate = now.plusMonths(12l);

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
						startDate = startDate.plusDays(7l);
					} while (startDate.isBefore(endOfPlanDate));
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates {@link AMOUNT_OF_CUSTOMERS} demo customer accounts. (This method
	 * shall be used solely for the demo mode.)
	 */
	private void initCustomers() {
		for (int i = 0; i < AMOUNT_OF_CUSTOMERS; i++) {
			keycloakApiService.createCustomerUser("customer" + (i + 1));
		}
	}

	/**
	 * Creates {@link BOOKINGS_PER_CUSTOMER} random demo bookings for every
	 * customer. (This method shall be used solely for the demo mode.)
	 */
	private void initBookings() {
		List<UserRepresentation> customers = keycloakApiService
				.getCustomerUsers();
		List<Flight> flights = new ArrayList<>();
		flightRepository.findAll().forEach(flights::add);
		final int[] baggageWeights = {0, 15, 23, 30};

		Random random = new Random(444);
		for (UserRepresentation customer : customers) {
			for (int i = 0; i < BOOKINGS_PER_CUSTOMER; i++) {
				// Flight
				Flight flight = flights.get(random.nextInt(flights.size()));

				// Create travelers
				IncomingTravelerDto[] travelers = new IncomingTravelerDto[random
						.nextInt(12) + 1];
				SelectedSeatDto[] selectedSeats = new SelectedSeatDto[travelers.length];
				for (int j = 0; j < travelers.length; j++) {
					travelers[j] = new IncomingTravelerDto("Max", "Mustermann",
							false,
							baggageWeights[random
									.nextInt(baggageWeights.length)],
							baggageWeights[random
									.nextInt(baggageWeights.length)],
							baggageWeights[random
									.nextInt(baggageWeights.length)],
							baggageWeights[random
									.nextInt(baggageWeights.length)]);
					selectedSeats[j] = new SelectedSeatDto(
							random.nextInt(flight.getPlane().getTypeData()
									.getNumberOfRows()),
							random.nextInt(flight.getPlane().getTypeData()
									.getNumberOfColumns()));
				}

				// Try to book the flight (may fail if seat is already taken by
				// another traveler)
				IncomingBookingDto booking = new IncomingBookingDto(
						flight.getId(), travelers, selectedSeats, 0.0);
				bookingService.bookFlight(customer.getId(), booking);
			}
		}
	}

	/**
	 * Creates the required employee accounts.
	 */
	private void initEmployees() {
		// Pilots
		// TODO: differentiate dash and jet pilots
		for (int i = 0; i < 40; i++) {
			keycloakApiService.createEmployeeUser("dashpilot" + (i + 1),
					EmployeeRole.PILOT);
		}
		for (int i = 0; i < 10; i++) {
			keycloakApiService.createEmployeeUser("jetpilot" + (i + 1),
					EmployeeRole.PILOT);
		}

		// Cabin crew
		// TODO: add cabin crew role
		for (int i = 0; i < 200; i++) {
			keycloakApiService.createEmployeeUser("cabin" + (i + 1),
					EmployeeRole.DEFAULT);
		}

		for (int i = 0; i < 30; i++) {
			keycloakApiService.createEmployeeUser("technician" + (i + 1),
					EmployeeRole.TECHNICIAN);
		}

		for (int i = 0; i < 20; i++) {
			keycloakApiService.createEmployeeUser("ground" + (i + 1),
					EmployeeRole.GROUND);
		}
	}

	/**
	 * Requests a vacation for every employee.
	 */
	private void initVacation() {
		List<UserRepresentation> employees = keycloakApiService
				.getEmployeeUsers();
		Random random = new Random(42);

		for (UserRepresentation employee : employees) {
			int offsetInDays = random.nextInt(340) + 1;
			RequestVacationDto requestVacationDto = new RequestVacationDto(
					OffsetDateTime.now().plusDays(offsetInDays),
					OffsetDateTime.now()
							.plusDays(offsetInDays + random.nextInt(14) + 1));
			vacationService.requestVacation(employee.getUsername(),
					requestVacationDto);
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
