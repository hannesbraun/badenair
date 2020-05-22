package de.hso.badenair.service.keycloakapi;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum EmployeeRole {
    DEFAULT("badenair_employee"),
    PILOT("PILOT"),
    FLIGHT_DIRECTOR("FLIGHT_DIRECTOR"),
    GROUND("GROUND"),
    TECHNICIAN("TECHNICIAN");

    private final String name;

    public static EmployeeRole fromString(String s) {
        return Arrays.stream(EmployeeRole.values())
            .filter(name -> name.getName().equalsIgnoreCase(s))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unknown role: " + s));
    }
}
