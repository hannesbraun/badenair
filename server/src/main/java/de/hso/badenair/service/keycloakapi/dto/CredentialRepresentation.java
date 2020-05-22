package de.hso.badenair.service.keycloakapi.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CredentialRepresentation {
    boolean temporary;
    String type;
    String value;
}
