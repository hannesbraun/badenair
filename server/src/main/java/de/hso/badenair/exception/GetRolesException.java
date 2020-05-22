package de.hso.badenair.exception;

public class GetRolesException extends RuntimeException {
    public GetRolesException() {
        super("Can't get roles from Keycloak");
    }
}
