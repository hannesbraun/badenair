package de.hso.badenair.controller.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.hso.badenair.controller.dto.account.UpdateAccountDataDto;
import de.hso.badenair.domain.booking.account.AccountData;
import de.hso.badenair.service.account.AccountDataRepository;
import de.hso.badenair.service.account.AccountService;
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

import java.security.Principal;
import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
            .customerUserId("user")
            .cardNumber(cardNumber)
            .build();

        accountDataRepository.save(accountData);

        mvc.perform(get(API_URL)
            .principal(principal))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.cardNumber", is(cardNumber)));
    }

    @Test
    void testThatDataIsSavedWhenFinishingRegistration() throws Exception {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn("user");

        final UpdateAccountDataDto dto = new UpdateAccountDataDto(
            OffsetDateTime.now().minusDays(10),
            "1234",
            "12344",
            "1234",
            "1234",
            "1234",
            "1234",
            OffsetDateTime.now().plusDays(10)
        );

        mvc.perform(put(API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto))
            .principal(principal))
            .andExpect(status().isOk());

        final Optional<AccountData> data = accountDataRepository.findByCustomerUserId("user");

        assertThat(data).isPresent();
    }


    @Configuration
    @EnableJpaRepositories(basePackageClasses = {AccountDataRepository.class})
    @EntityScan(basePackageClasses = {AccountData.class})
    @ComponentScan(basePackageClasses = {AccountService.class})
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
