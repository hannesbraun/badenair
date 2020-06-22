package de.hso.badenair.util.init;

import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import de.hso.badenair.domain.flight.FlightCrewMember;
import de.hso.badenair.domain.flight.FlightState;
import de.hso.badenair.domain.flight.ScheduledFlight;
import de.hso.badenair.domain.plane.Plane;
import de.hso.badenair.domain.plane.PlaneState;
import de.hso.badenair.domain.plane.PlaneType;
import de.hso.badenair.domain.plane.PlaneTypeData;
import de.hso.badenair.domain.schedule.ShiftSchedule;
import de.hso.badenair.domain.schedule.StandbySchedule;
import de.hso.badenair.service.airport.AirportRepository;
import de.hso.badenair.service.flight.booking.BookingService;
import de.hso.badenair.service.flight.repository.FlightCrewMemberRepository;
import de.hso.badenair.service.flight.repository.FlightRepository;
import de.hso.badenair.service.flight.repository.ScheduledFlightRepository;
import de.hso.badenair.service.keycloakapi.EmployeeRole;
import de.hso.badenair.service.keycloakapi.KeycloakApiService;
import de.hso.badenair.service.keycloakapi.dto.UserRepresentation;
import de.hso.badenair.service.plan.repository.StandbyScheduleRepository;
import de.hso.badenair.service.plan.shift.ShiftPlanRepository;
import de.hso.badenair.service.plan.vacation.VacationRepository;
import de.hso.badenair.service.plan.vacation.VacationService;
import de.hso.badenair.service.plane.repository.PlaneRepository;
import de.hso.badenair.service.plane.repository.PlaneTypeDataRepository;
import de.hso.badenair.util.csv.CsvHelper;
import de.hso.badenair.util.time.DateFusioner;
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

	private final FlightCrewMemberRepository flightCrewMemberRepository;

	private final VacationRepository vacationRepository;

	private final KeycloakApiService keycloakApiService;

	private final ShiftPlanRepository shiftPlanRepository;

	private final StandbyScheduleRepository standbyScheduleRepository;

	private final BookingService bookingService;

	private final VacationService vacationService;

	/**
	 * If set to true, demo mode will be enabled.
	 */
	private static final boolean DEMO_MODE = false;

	/**
	 * Defines the amount of customer accounts that will be created in the demo
	 * mode.
	 */
	private static final int AMOUNT_OF_CUSTOMERS = 1040;

	/**
	 * Defines the amount of bookings that will be generated for a customer in the
	 * demo mode.
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

		initShiftPlan();
		initStandbyPlan();
		initFlightCrews();
	}

	/**
	 * Creates the available plane types in the database.
	 */
	private void initPlaneTypeData() {
		final PlaneTypeData planeTypeDataDash_8_400 = PlaneTypeData.builder().type(PlaneType.Dash_8_400)
				.numberOfPassengers(76).numberOfRows(19).numberOfColumns(4).flightRange(1000).build();

		final PlaneTypeData planeTypeDataDash_8_200 = PlaneTypeData.builder().type(PlaneType.Dash_8_200)
				.numberOfPassengers(36).numberOfRows(9).numberOfColumns(4).flightRange(1000).build();

		final PlaneTypeData planeTypeDataB737_400 = PlaneTypeData.builder().type(PlaneType.B737_400)
				.numberOfPassengers(186).numberOfRows(31).numberOfColumns(6).flightRange(2500).build();

		planeTypeDataRepository
				.saveAll(List.of(planeTypeDataDash_8_400, planeTypeDataDash_8_200, planeTypeDataB737_400));
	}

	/**
	 * Creates the available planes in the database.
	 */
	private void initPlanes() {
		// Get types
		PlaneTypeData planeTypeDataDash_8_200 = planeTypeDataRepository.findAllByType(PlaneType.Dash_8_200).get(0);
		PlaneTypeData planeTypeDataDash_8_400 = planeTypeDataRepository.findAllByType(PlaneType.Dash_8_400).get(0);
		PlaneTypeData planeTypeDataB737_400 = planeTypeDataRepository.findAllByType(PlaneType.B737_400).get(0);

		List<Plane> planes = new ArrayList<Plane>();

		// Number of available plane
		final int dash_8_200_count = 3;
		final int dash_8_400_count = 5;
		final int b737_400_count = 2;

		// Create planes
		for (int i = 0; i < dash_8_200_count; i++) {
			planes.add(Plane.builder().typeData(planeTypeDataDash_8_200).state(PlaneState.WAITING).traveledDistance(0)
					.build());
		}

		for (int i = 0; i < dash_8_400_count; i++) {
			planes.add(Plane.builder().typeData(planeTypeDataDash_8_400).state(PlaneState.WAITING).traveledDistance(0)
					.build());
		}

		for (int i = 0; i < b737_400_count; i++) {
			planes.add(Plane.builder().typeData(planeTypeDataB737_400).state(PlaneState.WAITING).traveledDistance(0)
					.build());
		}

		planeRepository.saveAll(planes);
	}

	/**
	 * Creates the available airports in the database.
	 */
	private void initAirports() {
		List<Airport> airports = new ArrayList<Airport>();

		try {
			List<String[]> data = CsvHelper.getData(ResourceUtils.getFile("classpath:data/airports.csv"));

			// Get all (valid) airports
			for (String[] airportData : data) {
				if (airportData.length == 3) {
					airports.add(Airport.builder().name(airportData[0]).distance(Integer.valueOf(airportData[1]))
							.timezone(airportData[2]).build());
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		airportRepository.saveAll(airports);
	}

	/**
	 * Initializes the flightplan data according to the file "flightplan.csv". If
	 * {@link DEMO_MODE} is set, a more dense flightplan will be used.
	 */
	private void initFlightplan() {
		// Get planes
		List<Plane> initialPlanes = new LinkedList<Plane>();
		planeRepository.findAll().forEach(initialPlanes::add);
		HashMap<String, Plane> mappedPlanes = new HashMap<String, Plane>();

		List<ScheduledFlight> scheduledFlights = new ArrayList<>();
		List<Flight> flights = new ArrayList<>();

		// Demo mode: adjusted flightplan
		String flightplanFile;
		if (!DEMO_MODE) {
			flightplanFile = "flightplan.csv";
		} else {
			flightplanFile = "flightplanDemo.csv";
		}

		try {
			List<String[]> data = CsvHelper.getData(ResourceUtils.getFile("classpath:data/" + flightplanFile));

			// Map ids of planes (in case they change)
			for (String[] flightData : data) {
				if (flightData.length == 8) {
					if (mappedPlanes.containsKey(flightData[7])) {
						// Id already mapped
						continue;
					}

					// Find unmapped plane
					for (Plane plane : initialPlanes) {
						if (plane.getTypeData().getType().getName().equals(flightData[6])) {
							mappedPlanes.put(flightData[7], plane);
						}
					}
					initialPlanes.remove(mappedPlanes.get(flightData[7]));
				}
			}

			// Dates stored for consistency
			OffsetDateTime now = OffsetDateTime.now().withOffsetSameLocal(ZoneOffset.of("+1"));
			OffsetDateTime endOfPlanDate = now.plusMonths(12l);

			// Get all (valid) scheduled flights
			for (String[] flightData : data) {
				if (flightData.length == 8) {
					int hour = Integer.valueOf(flightData[4].substring(0, flightData[4].indexOf(":")));
					int minute = Integer.valueOf(flightData[4].substring(flightData[4].indexOf(":") + 1));

					ScheduledFlight scheduledFlight = ScheduledFlight.builder()
							.startingAirport(airportRepository.findByName(flightData[0]).get())
							.destinationAirport(airportRepository.findByName(flightData[1]).get())
							.basePrice(Double.valueOf(flightData[2])).durationInHours(Double.valueOf(flightData[3]))
							.startTime(now.withHour(hour).withMinute(minute)).build();
					scheduledFlights.add(scheduledFlight);

					Plane plane = mappedPlanes.get(flightData[7]);

					OffsetDateTime startDate = now
							.plusDays(Math.floorMod(Integer.valueOf(flightData[5]) - now.getDayOfWeek().getValue(), 7));
					do {
						// Add flights for the next 12 months
						if (DateFusioner.fusionStartDate(startDate, scheduledFlight.getStartTime(), null)
								.isAfter(OffsetDateTime.now().withOffsetSameLocal(ZoneOffset.of("+1")))) {
							// Flight is in the future
							flights.add(Flight.builder().scheduledFlight(scheduledFlight).state(FlightState.OK)
									.plane(plane).startDate(startDate).delay(0.0).build());
						} else if (DateFusioner
								.fusionArrivalDate(startDate, scheduledFlight.getStartTime(),
										Double.valueOf(flightData[3]), null)
								.isAfter(OffsetDateTime.now().withOffsetSameLocal(ZoneOffset.of("+1")))) {
							// Plane is flying
							flights.add(Flight.builder().scheduledFlight(scheduledFlight).state(FlightState.OK)
									.plane(plane).startDate(startDate).actualStartTime(DateFusioner
											.fusionStartDate(startDate, scheduledFlight.getStartTime(), null))
									.delay(0.0).build());
						} else {
							// Flight is already finished
							flights.add(Flight.builder().scheduledFlight(scheduledFlight).state(FlightState.OK)
									.plane(plane).startDate(startDate)
									.actualStartTime(DateFusioner.fusionStartDate(startDate,
											scheduledFlight.getStartTime(), null))
									.actualLandingTime(DateFusioner.fusionArrivalDate(startDate,
											scheduledFlight.getStartTime(), Double.valueOf(flightData[3]), null))
									.delay(0.0).build());
						}
						startDate = startDate.plusDays(7l);
					} while (startDate.isBefore(endOfPlanDate));
				}
			}

			// Save to database
			scheduledFlightRepository.saveAll(scheduledFlights);
			flightRepository.saveAll(flights);
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
		List<UserRepresentation> customers = keycloakApiService.getCustomerUsers();
		List<Flight> flights = new ArrayList<>();
		flightRepository.findAll().forEach(flights::add);
		final int[] baggageWeights = { 0, 15, 23, 30 };

		Random random = new Random(444);
		for (UserRepresentation customer : customers) {
			for (int i = 0; i < BOOKINGS_PER_CUSTOMER; i++) {
				// Flight
				Flight flight = flights.get(random.nextInt(flights.size()));

				// Create travelers
				IncomingTravelerDto[] travelers = new IncomingTravelerDto[random.nextInt(12) + 1];
				SelectedSeatDto[] selectedSeats = new SelectedSeatDto[travelers.length];
				for (int j = 0; j < travelers.length; j++) {
					travelers[j] = new IncomingTravelerDto("Max", "Mustermann", false,
							baggageWeights[random.nextInt(baggageWeights.length)],
							baggageWeights[random.nextInt(baggageWeights.length)],
							baggageWeights[random.nextInt(baggageWeights.length)],
							baggageWeights[random.nextInt(baggageWeights.length)]);
					selectedSeats[j] = new SelectedSeatDto(
							random.nextInt(flight.getPlane().getTypeData().getNumberOfRows()),
							random.nextInt(flight.getPlane().getTypeData().getNumberOfColumns()));
				}

				// Try to book the flight (may fail if seat is already taken by
				// another traveler)
				IncomingBookingDto booking = new IncomingBookingDto(flight.getId(), travelers, selectedSeats, 0.0);
				bookingService.bookFlight(customer.getId(), booking);
			}
		}
	}

	/**
	 * Creates the required employee accounts.
	 */
	private void initEmployees() {
		// Pilots
		for (int i = 0; i < 40; i++) {
			keycloakApiService.createEmployeeUser("dashpilot" + (i + 1), EmployeeRole.DASH_PILOT);
		}
		for (int i = 0; i < 10; i++) {
			keycloakApiService.createEmployeeUser("jetpilot" + (i + 1), EmployeeRole.JET_PILOT);
		}

		// Cabin crew
		for (int i = 0; i < 200; i++) {
			keycloakApiService.createEmployeeUser("cabin" + (i + 1), EmployeeRole.CABIN);
		}

		for (int i = 0; i < 30; i++) {
			keycloakApiService.createEmployeeUser("technician" + (i + 1), EmployeeRole.TECHNICIAN);
		}

		for (int i = 0; i < 20; i++) {
			keycloakApiService.createEmployeeUser("ground" + (i + 1), EmployeeRole.GROUND);
		}

		for (int i = 0; i < 1; i++) {
			keycloakApiService.createEmployeeUser("flightdirector" + (i + 1), EmployeeRole.FLIGHT_DIRECTOR);
		}
	}

	/**
	 * Creates initial shift plan.
	 */
	private void initShiftPlan() {
		int technicianCount = 30;
		int groundCount = 20;

		List<UserRepresentation> employees = keycloakApiService.getEmployeeUsers();

		OffsetDateTime date = OffsetDateTime.now().withDayOfMonth(1);
		Month currentMonth = date.getMonth();

		while (date.getMonth() == currentMonth) {
			ShiftSchedule.ShiftScheduleBuilder fruehschicht = ShiftSchedule.builder()
					.startTime(date.withHour(6).withMinute(0).withSecond(0).withNano(0))
					.endTime(OffsetDateTime.now().withHour(14).withMinute(0).withSecond(0).withNano(0));
			ShiftSchedule.ShiftScheduleBuilder spaetschicht = ShiftSchedule.builder()
					.startTime(date.withHour(14).withMinute(0).withSecond(0).withNano(0))
					.endTime(date.withHour(22).withMinute(0).withSecond(0).withNano(0));
			ShiftSchedule.ShiftScheduleBuilder nachtschicht = ShiftSchedule.builder()
					.startTime(date.withHour(22).withMinute(0).withSecond(0).withNano(0))
					.endTime(date.withHour(23).plusHours(7).withMinute(0).withSecond(0).withNano(0));

			int techniciansPlanned = 0;
			int groundsPlanned = 0;

			for (UserRepresentation employee : employees) {
				String username = employee.getUsername();

				OffsetDateTime finalDate = date;
				if (vacationRepository.findByEmployeeUserIdOrderByStartTimeAsc(employee.getId()).stream()
						.anyMatch(v -> v.isOverlapping(finalDate, finalDate))) {
					continue;
				}

				if (date.getDayOfWeek().getValue() <= 5) {
					if (username.contains("ground")) {
						if (groundsPlanned < groundCount / 3) {
							shiftPlanRepository.save(fruehschicht.employeeUserId(employee.getId()).build());
						} else if (groundsPlanned < (2 * groundCount) / 3) {
							shiftPlanRepository.save(spaetschicht.employeeUserId(employee.getId()).build());
						} else {
							shiftPlanRepository.save(nachtschicht.employeeUserId(employee.getId()).build());
						}

						groundsPlanned++;
					}
				}

				if (username.contains("technician")) {
					if (techniciansPlanned < technicianCount / 3) {
						shiftPlanRepository.save(fruehschicht.employeeUserId(employee.getId()).build());
					} else if (techniciansPlanned < (2 * technicianCount) / 3) {
						shiftPlanRepository.save(spaetschicht.employeeUserId(employee.getId()).build());
					} else {
						shiftPlanRepository.save(nachtschicht.employeeUserId(employee.getId()).build());
					}

					techniciansPlanned++;
				} else if (username.contains("flightdirector")) {
					shiftPlanRepository.save(fruehschicht.employeeUserId(employee.getId()).build());
					shiftPlanRepository.save(spaetschicht.employeeUserId(employee.getId()).build());
					shiftPlanRepository.save(nachtschicht.employeeUserId(employee.getId()).build());
				}
			}

			date = date.plusDays(1);
		}
	}

	/**
	 * Creates initial standby plan.
	 */
	private void initStandbyPlan() {
		List<UserRepresentation> employees = keycloakApiService.getEmployeeUsers();
		List<UserRepresentation> technicians = employees.stream().filter(e -> e.getUsername().contains("technician"))
				.collect(Collectors.toList());
		List<UserRepresentation> grounds = employees.stream().filter(e -> e.getUsername().contains("ground"))
				.collect(Collectors.toList());

		int techniciansPlanned = 0;
		int groundsPlanned = 0;

		OffsetDateTime date = OffsetDateTime.now().withDayOfMonth(1);
		Month currentMonth = date.getMonth();

		while (date.getMonth() == currentMonth) {
			StandbySchedule.StandbyScheduleBuilder standbySchedule = StandbySchedule.builder()
					.startTime(date.withHour(0).withMinute(0).withSecond(0).withNano(0))
					.endTime(date.withHour(23).plusHours(1).withMinute(0).withSecond(0).withNano(0));

			OffsetDateTime finalDate = date;

			for (int i = 0; i < 1; i++) {
				String employeeId = technicians.get(techniciansPlanned).getId();

				if (vacationRepository.findByEmployeeUserIdOrderByStartTimeAsc(employeeId).stream()
						.anyMatch(v -> v.isOverlapping(finalDate, finalDate))) {
					continue;
				}

				standbyScheduleRepository.save(standbySchedule.employeeUserId(employeeId).build());
				techniciansPlanned = (techniciansPlanned + 1) % technicians.size();
			}

			for (int i = 0; i < 2; i++) {
				String employeeId = grounds.get(groundsPlanned).getId();

				if (vacationRepository.findByEmployeeUserIdOrderByStartTimeAsc(employeeId).stream()
						.anyMatch(v -> v.isOverlapping(finalDate, finalDate))) {
					continue;
				}

				standbyScheduleRepository.save(standbySchedule.employeeUserId(employeeId).build());
				groundsPlanned = (groundsPlanned + 1) % grounds.size();
			}

			date = date.plusDays(1);
		}
	}

	/**
	 * Creates flight crews and assigns them to flights.
	 */
	private void initFlightCrews() {
		List<UserRepresentation> employees = keycloakApiService.getEmployeeUsers();
		Iterator<UserRepresentation> pilots = employees.stream().filter(e -> e.getUsername().contains("pilot"))
				.iterator();
		Iterator<UserRepresentation> cabins = employees.stream().filter(e -> e.getUsername().contains("cabin"))
				.iterator();

		Map<String, List<CrewData>> crews = new HashMap<>() {
			{
				put("dash", new ArrayList<>());
				put("jet", new ArrayList<>());
			}
		};

		// Construct crews
		crews.get("dash").add(new CrewData());
		crews.get("jet").add(new CrewData());

		while (pilots.hasNext()) {
			UserRepresentation pilot = pilots.next();

			List<CrewData> currentCrewSet = crews.get(pilot.getUsername().contains("dash") ? "dash" : "jet");
			List<String> currentCrew = currentCrewSet.get(currentCrewSet.size() - 1).getEmployees();

			currentCrew.add(pilot.getId());

			if (currentCrew.size() == 2) {
				for (int i = 0; i < 4; i++) {
					if (!cabins.hasNext()) {
						break;
					}

					currentCrew.add(cabins.next().getId());
				}

				currentCrewSet.add(new CrewData());
			}
		}

		crews.get("dash").remove(crews.get("dash").size() - 1);
		crews.get("jet").remove(crews.get("jet").size() - 1);

		List<FlightGroup> flightGroups = new ArrayList<>();

		for (ScheduledFlight scheduledFlight : scheduledFlightRepository.findByStartingAirportId(1)) {
			Airport startingAirport = scheduledFlight.getStartingAirport();
			Airport destinationAirport = scheduledFlight.getDestinationAirport();

			LocalDate firstFlightDate = flightRepository.findByScheduledFlightId(scheduledFlight.getId()).get(0)
					.getStartDate().toLocalDate();
			OffsetDateTime landingTime = scheduledFlight.getLandingTime(null);

			Optional<ScheduledFlight> scheduledReturnFlightOpt = scheduledFlightRepository
					.findByStartingAirportIdAndDestinationAirportId(destinationAirport.getId(), startingAirport.getId())
					.stream().filter(f -> Duration.between(landingTime, f.getStartTime()).getSeconds() > 0)
					.sorted(Comparator.comparing(f -> Duration.between(landingTime, f.getStartTime())))
					.filter(f -> firstFlightDate.isEqual(
							flightRepository.findByScheduledFlightId(f.getId()).get(0).getStartDate().toLocalDate()))
					.findFirst();

			if (scheduledReturnFlightOpt.isEmpty()) {
				continue;
			}

			ScheduledFlight scheduledReturnFlight = scheduledReturnFlightOpt.get();

			List<Flight> currentFlights = flightRepository.findByScheduledFlightId(scheduledFlight.getId());
			List<Flight> currentReturnFlights = flightRepository.findByScheduledFlightId(scheduledReturnFlight.getId());

			OffsetTime flightStartTime = scheduledFlight.getStartTime().toOffsetTime();
			OffsetDateTime nextMonth = OffsetDateTime.now().plusMonths(1);

			for (int i = 0; i < currentFlights.size(); i++) {
				Flight flight = currentFlights.get(i);
				Flight returnFlight = currentReturnFlights.get(i);

				if (flight.getStartDate().isAfter(nextMonth)) {
				    continue;
                }

                flight.setStartDate(flight.getStartDate().toLocalDate().atTime(flightStartTime));
                returnFlight.setStartDate(scheduledReturnFlight.getLandingTime(returnFlight.getStartDate().toLocalDate()));

				flightGroups.add(new FlightGroup(flight, returnFlight));
			}
		}

		flightGroups.sort(Comparator.comparing(g -> g.getFlight().getStartDate()));

		final Map<String, Integer> maxCrewAssignments = new HashMap<>() {
            {
                put("dash", 0);
                put("jet", 0);
            }
        };

		for (FlightGroup flightGroup : flightGroups) {
			Flight flight = flightGroup.getFlight();
			Flight returnFlight = flightGroup.getReturnFlight();

			OffsetDateTime flightStartDateTime = flight.getStartDate();
			OffsetDateTime returnFlightLandingDateTime = returnFlight.getStartDate();

            String planeName = flight.getPlane().getTypeData().getType().getName().contains("Dash") ? "dash" : "jet";

			Stream<CrewData> crewsStream = crews.get(planeName).stream();

            // 8 hours per day and 20% overtime
			final double allowedHoursPerDay = 8.0 * 1.2;

			List<CrewData> possibleCrews = crewsStream
					.filter(c -> (c.busyUntil == null || c.busyUntil.isBefore(flightStartDateTime))
                            && c.getHoursAtDay(
									flightStartDateTime) < allowedHoursPerDay
											- (Duration
													.between(flightStartDateTime.minusMinutes(30l),
															returnFlightLandingDateTime.plusMinutes(30l))
													.getSeconds() / 3600.0)
                            && c.getEmployees().stream().allMatch(e -> vacationRepository
                            .findByEmployeeUserIdOrderByStartTimeAsc(e).stream()
                            .noneMatch(v -> v.isOverlapping(flightStartDateTime, returnFlightLandingDateTime)))).collect(Collectors.toList());

			int finalMaxCrewAssignments = maxCrewAssignments.get(planeName);

			Optional<CrewData> foundCrewOpt = possibleCrews.stream().filter(c -> c.assignments < finalMaxCrewAssignments).findFirst();

			if(foundCrewOpt.isEmpty()) {
			    foundCrewOpt = possibleCrews.stream().filter(c -> c.assignments <= finalMaxCrewAssignments).findFirst();
            }

			if (foundCrewOpt.isPresent()) {
				CrewData foundCrew = foundCrewOpt.get();
				foundCrew.assignments++;

				if(foundCrew.assignments > finalMaxCrewAssignments) {
				    maxCrewAssignments.put(planeName, foundCrew.assignments);
                }

				for (String employeeId : foundCrew.getEmployees()) {
					shiftPlanRepository.save(ShiftSchedule.builder().startTime(flightStartDateTime.minusMinutes(30l))
							.endTime(returnFlightLandingDateTime.plusMinutes(30l)).employeeUserId(employeeId).build());

					flightCrewMemberRepository
							.save(FlightCrewMember.builder().flight(flight).employeeUserId(employeeId).build());
					flightCrewMemberRepository
							.save(FlightCrewMember.builder().flight(returnFlight).employeeUserId(employeeId).build());
				}

				foundCrew.setBusyUntil(returnFlightLandingDateTime.plusHours(1l));
				if (flightStartDateTime.getDayOfMonth() == returnFlightLandingDateTime.getDayOfMonth()) {
					foundCrew
							.addHourAtDay(
									flightStartDateTime, Duration
											.between(flightStartDateTime.minusMinutes(30l),
													returnFlightLandingDateTime.plusMinutes(30l))
											.getSeconds() / 3600.0);
				} else {
					foundCrew.addHourAtDay(flightStartDateTime,
							(86400.0 - flightStartDateTime.minusMinutes(30l).get(ChronoField.SECOND_OF_DAY)) / 3600.0);
					foundCrew.addHourAtDay(returnFlightLandingDateTime,
							returnFlightLandingDateTime.plusMinutes(30l).get(ChronoField.SECOND_OF_DAY) / 3600.0);
				}
			} else {
				System.err.println("Warning: no flight crew available for flight " + flight.getId());
			}
		}
	}

	/**
	 * Requests a vacation for every employee.
	 */
	private void initVacation() {
		List<UserRepresentation> employees = keycloakApiService.getEmployeeUsers();
		Random random = new Random(42);

		for (UserRepresentation employee : employees) {
			int offsetInDays = random.nextInt(340) + 1;
			RequestVacationDto requestVacationDto = new RequestVacationDto(OffsetDateTime.now().plusDays(offsetInDays),
					OffsetDateTime.now().plusDays(offsetInDays + random.nextInt(14) + 1));
			vacationService.requestVacation(employee.getId(), requestVacationDto);
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
