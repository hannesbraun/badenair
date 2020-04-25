package de.hso.badenair.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import de.hso.badenair.controller.boardingpass.BoardingPassController;
import de.hso.badenair.domain.booking.Booking;
import de.hso.badenair.domain.booking.Luggage;
import de.hso.badenair.domain.booking.LuggageState;
import de.hso.badenair.domain.booking.Traveler;
import de.hso.badenair.domain.flight.Airport;
import de.hso.badenair.domain.flight.Flight;
import de.hso.badenair.domain.flight.ScheduledFlight;
import de.hso.badenair.domain.plane.Plane;
import de.hso.badenair.domain.plane.PlaneState;
import de.hso.badenair.domain.plane.PlaneType;
import de.hso.badenair.service.boardingpass.BoardingPassService;
import de.hso.badenair.service.luggage.LuggageRepository;
import de.hso.badenair.service.plane.repository.PlaneTypeDataRepository;
import de.hso.badenair.service.traveler.TravelerRepository;
import de.hso.badenair.util.init.StaticDataInitializer;

@DataJpaTest
@ContextConfiguration(classes = BoardingPassControllerTest.TestConfig.class)
@ExtendWith(MockitoExtension.class)
class BoardingPassControllerTest {

	private final String API_URL = "/api/customer/boardingpass";

	@MockBean
	private TravelerRepository travelerRepository;

	@MockBean
	private LuggageRepository luggageRepository;

	@Autowired
	private PlaneTypeDataRepository planeTypeDataRepository;

	@Autowired
	private BoardingPassController uut;

	private MockMvc mvc;

	@BeforeEach
	void setUp() {
		mvc = standaloneSetup(uut).build();
	}

	@Test
	void testNonExistingTraveler() throws Exception {
		mvc.perform(get(API_URL).contentType(MediaType.APPLICATION_JSON)
				.param("travelerId", "12345")).andExpect(status().isNotFound());
	}

	@Test
	void testGetBoardingPass() throws Exception {
		Airport startingAirport = Airport.builder().id(1l)
				.name("Basel-Mulhouse").timezone("+1").build();
		Airport destinationAirport = Airport.builder().id(2l).name("Porto")
				.timezone("+2").build();
		Plane plane = Plane.builder().id(1l)
				.typeData(planeTypeDataRepository
						.findAllByType(PlaneType.B737_400).get(0))
				.state(PlaneState.WAITING).build();
		ScheduledFlight scheduledFlight = ScheduledFlight.builder().id(1l)
				.startingAirport(startingAirport)
				.destinationAirport(destinationAirport)
				.startTime(OffsetDateTime.of(2020, 1, 1, 6, 50, 0, 0,
						ZoneOffset.of("+1")))
				.durationInHours(2.5833333333333333).build();
		Flight flight = Flight.builder().id(1l).scheduledFlight(scheduledFlight)
				.startDate(OffsetDateTime.of(2020, 4, 24, 0, 0, 0, 0,
						ZoneOffset.of("+1")))
				.plane(plane).build();
		Booking booking = Booking.builder().id(1l).flight(flight).build();
		Traveler traveler = Traveler.builder().id(1l).firstName("Bob")
				.lastName("Ross").seatNumber("21F").checkedIn(true)
				.booking(booking).build();

		Luggage luggage = Luggage.builder().id(1l)
				.state(LuggageState.AT_TRAVELLER).weight(23).traveler(traveler)
				.build();

		Mockito.when(travelerRepository.findById(Mockito.anyLong()))
				.thenReturn(Optional.of(traveler));
		Mockito.when(luggageRepository.findAllByTravelerId(Mockito.anyLong()))
				.thenReturn(List.of(luggage));

		mvc.perform(get(API_URL).contentType(MediaType.APPLICATION_JSON)
				.param("travelerId", traveler.getId().toString()))
				.andDo(print()).andExpect(status().isOk());
	}

	@Configuration
	@EnableJpaRepositories(basePackageClasses = {TravelerRepository.class,
			LuggageRepository.class, PlaneTypeDataRepository.class})
	@EntityScan(basePackageClasses = {Airport.class, Booking.class,
			Flight.class, ScheduledFlight.class, Luggage.class, Plane.class,
			Traveler.class})
	@ComponentScan(basePackageClasses = {BoardingPassService.class})
	@Import({BoardingPassController.class, StaticDataInitializer.class})
	static class TestConfig {
	}
}
