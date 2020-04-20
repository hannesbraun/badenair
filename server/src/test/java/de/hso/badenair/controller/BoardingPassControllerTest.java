package de.hso.badenair.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import de.hso.badenair.controller.boardingpass.BoardingPassController;
import de.hso.badenair.domain.booking.Booking;
import de.hso.badenair.domain.booking.Luggage;
import de.hso.badenair.domain.booking.LuggageState;
import de.hso.badenair.domain.booking.Traveler;
import de.hso.badenair.domain.flight.Airport;
import de.hso.badenair.domain.flight.Flight;
import de.hso.badenair.domain.flight.ScheduledFlight;
import de.hso.badenair.service.boardingpass.BoardingPassService;
import de.hso.badenair.service.luggage.LuggageRepository;
import de.hso.badenair.service.traveler.TravelerRepository;

@DataJpaTest
@ContextConfiguration(classes = BoardingPassControllerTest.TestConfig.class)
@ExtendWith(MockitoExtension.class)
class BoardingPassControllerTest {

	private final String API_URL = "/api/customer/boardingpass";

	@Autowired
	private TravelerRepository travelerRepository;

	@Autowired
	private LuggageRepository luggageRepository;

	@Autowired
	private ObjectMapper objectMapper;

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
				.content(objectMapper.writeValueAsString(Long.valueOf(12345))))
				.andExpect(status().isNotFound());
	}

	@Test
	void testGetBoardingPass() throws Exception {
		Airport startingAirport = Airport.builder().name("Basel-Mulhouse")
				.build();
		Airport destinationAirport = Airport.builder().name("Porto").build();
		ScheduledFlight scheduledFlight = ScheduledFlight.builder()
				.startingAirport(startingAirport)
				.destinationAirport(destinationAirport).startTime(OffsetDateTime
						.of(0, 0, 0, 6, 50, 0, 0, ZoneOffset.of("+2")))
				.durationInHours(2.35).build();
		Flight flight = Flight.builder().scheduledFlight(scheduledFlight)
				.startDate(OffsetDateTime.now(ZoneId.of("GMT+1"))).build();
		Booking booking = Booking.builder().flight(flight).build();
		Traveler traveler = Traveler.builder().firstName("Bob").lastName("Ross")
				.seatNumber("21F").checkedIn(true).booking(booking).build();
		final Traveler savedTraveler = travelerRepository.save(traveler);

		Luggage luggage = Luggage.builder().state(LuggageState.AT_TRAVELLER)
				.weight(23).traveler(traveler).build();
		luggageRepository.save(luggage);

		mvc.perform(
				get(API_URL).contentType(MediaType.APPLICATION_JSON).content(
						objectMapper.writeValueAsString(savedTraveler.getId())))
				.andDo(print()).andExpect(status().isOk());
	}

	@Configuration
	@EnableJpaRepositories(basePackageClasses = {TravelerRepository.class,
			LuggageRepository.class})
	@EntityScan(basePackageClasses = {Airport.class, ScheduledFlight.class,
			Flight.class, Traveler.class, Luggage.class})
	@ComponentScan(basePackageClasses = {BoardingPassService.class})
	@Import({BoardingPassController.class})
	static class TestConfig {
		@Bean
		public ObjectMapper objectMapper() {
			final ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule());
			return objectMapper;
		}
	}
}
