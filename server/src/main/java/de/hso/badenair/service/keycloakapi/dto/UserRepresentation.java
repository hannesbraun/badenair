package de.hso.badenair.service.keycloakapi.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class UserRepresentation {
    List<CredentialRepresentation> credentials;
    String email;
    boolean emailVerified;
    boolean enabled;
    String firstName;
    String id;
    String lastName;
    String username;
}
