package de.hso.badenair.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.hso.badenair.controller.dto.plan.RequestVacationDto;
import de.hso.badenair.domain.schedule.Vacation;
import de.hso.badenair.service.plan.vacation.VacationRepository;
import de.hso.badenair.service.plan.vacation.VacationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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

import java.security.Principal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@DataJpaTest
@ContextConfiguration(classes = PlanControllerIT.TestConfig.class)
class PlanControllerIT {

    private final String API_URL = "/api/employee/plan/vacation";

    @Autowired
    private VacationRepository vacationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PlanController uut;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = standaloneSetup(uut).build();
    }

    @Test
    void testThatGetVacationPlanReturnsNonEmptyList() throws Exception {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn("user");

        final Vacation vacation = Vacation.builder()
            .employeeUserId("user")
            .startTime(OffsetDateTime.now())
            .endTime(OffsetDateTime.now())
            .build();

        vacationRepository.save(vacation);

        mvc.perform(get(API_URL)
            .principal(principal))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testThatRequestVacationSavesRequest() throws Exception {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn("user");

        final RequestVacationDto dto = new RequestVacationDto(OffsetDateTime.now().plusDays(10), OffsetDateTime.now().plusDays(15));

        mvc.perform(post(API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto))
            .principal(principal))
            .andExpect(status().isCreated());

        final List<Vacation> vacations = vacationRepository.findByEmployeeUserIdOrderByStartTimeAsc("user");

        assertThat(vacations.size()).isEqualTo(1);
    }

    @Test
    void testThatRequestVacationDoesntSaveWhenDatesAreInvalid() throws Exception {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn("user");

        final RequestVacationDto dto = new RequestVacationDto(OffsetDateTime.now().plusDays(10), OffsetDateTime.now().plusDays(5));

        mvc.perform(post(API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto))
            .principal(principal))
            .andExpect(status().isCreated());

        final List<Vacation> vacations = vacationRepository.findByEmployeeUserIdOrderByStartTimeAsc("user");

        assertThat(vacations.size()).isEqualTo(0);
    }

    @Configuration
    @EnableJpaRepositories(basePackageClasses = {VacationRepository.class})
    @EntityScan(basePackageClasses = {Vacation.class})
    @ComponentScan(basePackageClasses = {VacationService.class})
    @Import({PlanController.class})
    static class TestConfig {
        @Bean
        public ObjectMapper objectMapper() {
            final ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper;
        }
    }
}
