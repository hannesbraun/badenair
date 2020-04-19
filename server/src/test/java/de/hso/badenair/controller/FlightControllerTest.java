package de.hso.badenair.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.hso.badenair.controller.flight.FlightController;
import de.hso.badenair.domain.flight.Flight;
import de.hso.badenair.service.flight.FlightRepository;
import de.hso.badenair.service.flight.FlightService;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@DataJpaTest
@ContextConfiguration(classes = FlightControllerTest.TestConfig.class)
@ExtendWith(MockitoExtension.class)
class FlightControllerTest {

    private final String API_URL = "/api/employee/luggage";

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FlightController uut;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = standaloneSetup(uut).build();
    }

    @Configuration
    @EnableJpaRepositories(basePackageClasses = {FlightRepository.class})
    @EntityScan(basePackageClasses = {Flight.class})
    @ComponentScan(basePackageClasses = {FlightService.class})
    @Import({FlightController.class})
    static class TestConfig {
        @Bean
        public ObjectMapper objectMapper() {
            final ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper;
        }
    }
}
