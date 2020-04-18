package de.hso.badenair.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.security.Principal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
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

import de.hso.badenair.controller.dto.luggage.LuggageStateDto;
import de.hso.badenair.controller.luggage.LuggageController;
import de.hso.badenair.domain.booking.Luggage;
import de.hso.badenair.domain.booking.LuggageState;
import de.hso.badenair.service.luggage.LuggageRepository;
import de.hso.badenair.service.luggage.LuggageService;

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
		Principal principal = Mockito.mock(Principal.class);
		when(principal.getName()).thenReturn("user");

		mvc.perform(get(API_URL).principal(principal))
				.andExpect(status().isNotFound());
	}

	@Test
	void testUpdatingState() throws Exception {
		Principal principal = Mockito.mock(Principal.class);
		when(principal.getName()).thenReturn("user");

		Luggage luggage = new Luggage(Long.valueOf(1234),
				LuggageState.AT_TRAVELLER, Integer.valueOf(23));
		luggageRepository.save(luggage);

		LuggageStateDto dto = new LuggageStateDto(Long.valueOf(1234),
				LuggageState.ON_BAGGAGE_CAROUSEL);
		mvc.perform(patch(API_URL).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))
				.principal(principal)).andExpect(status().isNoContent());

		luggage = luggageRepository.findById(Long.valueOf(1234)).get();

		assertThat(luggage.getState())
				.isEqualTo(LuggageState.ON_BAGGAGE_CAROUSEL);
	}

	@Configuration
	@EnableJpaRepositories(basePackageClasses = {LuggageRepository.class})
	@EntityScan(basePackageClasses = {Luggage.class})
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
