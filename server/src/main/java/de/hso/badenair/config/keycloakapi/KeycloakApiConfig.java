package de.hso.badenair.config.keycloakapi;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Valid
@Configuration
@ConfigurationProperties("keycloak")
@Getter
@Setter
@NoArgsConstructor
public class KeycloakApiConfig {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @NotEmpty
    private String host;
}
