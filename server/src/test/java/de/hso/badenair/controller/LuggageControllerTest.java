package de.hso.badenair.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.hso.badenair.controller.dto.luggage.LuggageStateDto;
import de.hso.badenair.controller.luggage.LuggageController;
import de.hso.badenair.domain.booking.Luggage;
import de.hso.badenair.domain.booking.LuggageState;
import de.hso.badenair.domain.booking.Traveler;
import de.hso.badenair.domain.flight.Flight;
import de.hso.badenair.domain.plane.Plane;
import de.hso.badenair.service.luggage.LuggageRepository;
import de.hso.badenair.service.luggage.LuggageService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@DataJpaTest
@ContextConfiguration(classes = LuggageControllerTest.TestConfig.class)
@ExtendWith(MockitoExtension.class)
class LuggageControllerTest {

	private final String API_URL = "/api/employee/luggage";

	@Autowired
	private LuggageRepository luggageRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private LuggageController uut;

	private MockMvc mvc;

	@BeforeEach
	void setUp() {
		mvc = standaloneSetup(uut).build();
	}

    @Test
    void testNonExistingLuggage() throws Exception {
        LuggageStateDto dto = new LuggageStateDto(1234L,
            LuggageState.ON_BAGGAGE_CAROUSEL);
        mvc.perform(patch(API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isNotFound());
    }

    @Test
    void testUpdatingState() throws Exception {
        Luggage luggage = Luggage.builder()
            .state(LuggageState.AT_TRAVELLER)
            .weight(23)
            .build();

        final Luggage savedLuggage = luggageRepository.save(luggage);

        LuggageStateDto dto = new LuggageStateDto(savedLuggage.getId(),
            LuggageState.ON_BAGGAGE_CAROUSEL);
        mvc.perform(patch(API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
            .andDo(print())
            .andExpect(status().isNoContent());

        luggage = luggageRepository.findById(savedLuggage.getId()).get();

		assertThat(luggage.getState())
				.isEqualTo(LuggageState.ON_BAGGAGE_CAROUSEL);
	}

	@Configuration
	@EnableJpaRepositories(basePackageClasses = {LuggageRepository.class})
	@EntityScan(basePackageClasses = {Luggage.class, Flight.class, Plane.class})
	@ComponentScan(basePackageClasses = {LuggageService.class})
	@Import({LuggageController.class})
	static class TestConfig {
		@Bean
		public ObjectMapper objectMapper() {
			final ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule());
			return objectMapper;
		}
	}
}
