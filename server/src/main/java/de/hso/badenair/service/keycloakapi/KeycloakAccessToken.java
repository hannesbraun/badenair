package de.hso.badenair.service.keycloakapi;

import lombok.Value;

@Value
public class KeycloakAccessToken {
    String access_token;
    Integer expires_in;
    String refresh_token;
}
