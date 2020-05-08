package de.hso.badenair.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.hso.badenair.controller.dto.plan.WorkingHoursDto;
import de.hso.badenair.controller.workinghours.WorkingHoursController;
import de.hso.badenair.domain.schedule.WorkingHours;
import de.hso.badenair.service.workinghours.WorkingHoursRepository;
import de.hso.badenair.service.workinghours.WorkingHoursService;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.security.Principal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@DataJpaTest
@ContextConfiguration(classes = WorkingHoursControllerTest.TestConfig.class)
@ExtendWith(MockitoExtension.class)
class WorkingHoursControllerTest {

    private final String API_URL = "/api/employee/workinghours";

    @Autowired
    private WorkingHoursRepository workingHoursRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WorkingHoursController uut;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = standaloneSetup(uut).build();
    }

    @Test
    void testGetLatestWorkingHours() throws Exception {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn("user");

        final WorkingHours workingHours = WorkingHours.builder()
            .employeeUserId("user")
            .build();

        workingHoursRepository.save(workingHours);

        final WorkingHoursDto dto = new WorkingHoursDto(workingHours.getStartTime(), workingHours.getEndTime());

        MvcResult result = mvc.perform(get(API_URL)
            .principal(principal))
            .andExpect(status().isOk())
            .andReturn();

        WorkingHoursDto returnedDto = objectMapper.readValue(result.getResponse().getContentAsString(), WorkingHoursDto.class);

        assertThat(returnedDto).isEqualTo(dto);
    }


    @Test
    void testTriggerWorkingHours() throws Exception {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn("user");

        mvc.perform(post(API_URL)
            .principal(principal))
            .andExpect(status().isOk()); // First trigger

        List<WorkingHours> workingHours = workingHoursRepository.findByEmployeeUserIdOrderByStartTimeDesc("user");

        // Assert that, new WorkingHours with a startTime and no endTime was created
        assertThat(workingHours.size()).isEqualTo(1);
        assertThat(workingHours.get(0).getStartTime()).isNotNull();
        assertThat(workingHours.get(0).getEndTime()).isNull();

        mvc.perform(post(API_URL)
            .principal(principal))
            .andExpect(status().isOk()); // Second trigger

        workingHours = workingHoursRepository.findByEmployeeUserIdOrderByStartTimeDesc("user");

        // Assert that, the WorkingHours created before has an endTime now
        assertThat(workingHours.size()).isEqualTo(1);
        assertThat(workingHours.get(0).getEndTime()).isNotNull();

        mvc.perform(post(API_URL)
            .principal(principal))
            .andExpect(status().isOk()); // Third trigger

        workingHours = workingHoursRepository.findByEmployeeUserIdOrderByStartTimeDesc("user");

        // Assert that, a new WorkingHours was created
        assertThat(workingHours.size()).isEqualTo(2);
    }

    @Configuration
    @EnableJpaRepositories(basePackageClasses = {WorkingHoursRepository.class})
    @EntityScan(basePackageClasses = {WorkingHours.class})
    @ComponentScan(basePackageClasses = {WorkingHoursService.class})
    @Import({WorkingHoursController.class})
    static class TestConfig {
        @Bean
        public ObjectMapper objectMapper() {
            final ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper;
        }
    }
}
