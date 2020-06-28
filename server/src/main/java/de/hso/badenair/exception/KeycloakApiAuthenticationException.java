package de.hso.badenair.exception;

public class KeycloakApiAuthenticationException extends RuntimeException {
    public KeycloakApiAuthenticationException() {
        super("Could not acquire access token");
    }
}
