package de.hso.badenair.controller.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.hso.badenair.controller.dto.account.UpdateAccountDataDto;
import de.hso.badenair.domain.booking.Booking;
import de.hso.badenair.domain.booking.Traveler;
import de.hso.badenair.domain.booking.account.AccountData;
import de.hso.badenair.domain.flight.Airport;
import de.hso.badenair.domain.flight.Flight;
import de.hso.badenair.domain.flight.ScheduledFlight;
import de.hso.badenair.domain.plane.Plane;
import de.hso.badenair.domain.plane.PlaneState;
import de.hso.badenair.domain.plane.PlaneType;
import de.hso.badenair.domain.plane.PlaneTypeData;
import de.hso.badenair.service.account.AccountDataRepository;
import de.hso.badenair.service.account.AccountService;
import de.hso.badenair.service.booking.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@DataJpaTest
@ContextConfiguration(classes = AccountControllerIT.TestConfig.class)
@ExtendWith(MockitoExtension.class)
class AccountControllerIT {

	private final String API_URL = "/api/customer/account";

	@Autowired
	private AccountDataRepository accountDataRepository;

	@MockBean
	private BookingRepository bookingRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private AccountController uut;

	private MockMvc mvc;

	@BeforeEach
	void setUp() {
		mvc = standaloneSetup(uut).build();
	}

	@Test
	void testThatGetAccountDataReturnsNonEmptyObject() throws Exception {
		Principal principal = Mockito.mock(Principal.class);
		when(principal.getName()).thenReturn("user");

		final String cardNumber = "1234";
		final AccountData accountData = AccountData.builder()
				.customerUserId("user").cardNumber(cardNumber).build();

		accountDataRepository.save(accountData);

		mvc.perform(get(API_URL).principal(principal))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.cardNumber", is(cardNumber)));
	}

	@Test
	void testThatDataIsSavedWhenFinishingRegistration() throws Exception {
		Principal principal = Mockito.mock(Principal.class);
		when(principal.getName()).thenReturn("user");

		final UpdateAccountDataDto dto = new UpdateAccountDataDto(
				OffsetDateTime.now().minusDays(10), "1234", "12344", "1234",
				"1234", "1234", "1234", OffsetDateTime.now().plusDays(10));

		mvc.perform(put(API_URL).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))
				.principal(principal)).andExpect(status().isOk());

		final Optional<AccountData> data = accountDataRepository
				.findByCustomerUserId("user");

		assertThat(data).isPresent();
	}

	@Test
	void testGetBookings() throws Exception {
		Principal principal = Mockito.mock(Principal.class);
		when(principal.getName()).thenReturn("user");

		// Create some data
		Airport startingAirport = Airport.builder().id(1l)
				.name("Basel-Mulhouse").timezone("+1").build();
		Airport destinationAirport = Airport.builder().id(2l).name("Porto")
				.timezone("+2").build();
		Plane plane = Plane.builder().id(1l)
				.typeData(PlaneTypeData.builder().id(1l).flightRange(1000)
						.type(PlaneType.Dash_8_200).numberOfPassengers(38)
						.build())
				.state(PlaneState.WAITING).build();
		ScheduledFlight scheduledFlight = ScheduledFlight.builder().id(1l)
				.startingAirport(startingAirport)
				.destinationAirport(destinationAirport)
				.startTime(OffsetDateTime.of(2020, 1, 1, 6, 50, 0, 0,
						ZoneOffset.of("+1")))
				.durationInHours(2.5833333333333333).build();
		Flight flight1 = Flight.builder().id(1l)
				.scheduledFlight(scheduledFlight)
				.startDate(OffsetDateTime.now().minusDays(10)).plane(plane)
				.build();
        Flight flight2 = Flight.builder().id(2l)
            .scheduledFlight(scheduledFlight)
            .startDate(OffsetDateTime.now().plusDays(10)).plane(plane)
            .build();
        Traveler traveler1 = Traveler.builder().id(1l).firstName("Bob")
            .lastName("Ross").seatColumn(6).seatRow(21).checkedIn(true).build();
        Booking booking1 = Booking.builder().id(1l).flight(flight1)
            .travelers(Set.of(traveler1)).build();
        Traveler traveler2 = Traveler.builder().id(2l).firstName("Bob")
            .lastName("Ross").seatColumn(6).seatRow(21).checkedIn(true).build();
        Booking booking2 = Booking.builder().id(2l).flight(flight2)
            .travelers(Set.of(traveler2)).build();

		// Mock repository
		Mockito.when(
				bookingRepository.findAllByCustomerUserId(Mockito.anyString()))
				.thenReturn(List.of(booking1, booking2));

		// Perform test
		mvc.perform(get("/api/customer/account/flights")
				.contentType(MediaType.APPLICATION_JSON).principal(principal))
				.andExpect(status().isOk());
	}

	@Configuration
	@EnableJpaRepositories(basePackageClasses = {AccountDataRepository.class,
			BookingRepository.class})
	@EntityScan(basePackageClasses = {AccountData.class})
	@ComponentScan(basePackageClasses = {AccountService.class, Airport.class,
			Booking.class, Flight.class, ScheduledFlight.class, Plane.class,
			Traveler.class})
	@Import({AccountController.class})
	static class TestConfig {
		@Bean
		public ObjectMapper objectMapper() {
			final ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule());
			return objectMapper;
		}
	}
}
