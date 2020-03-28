package de.hso.badenair.domain.flight;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum FlightState {
    OK("OK"),
    DELAYED("Verspätet");

    @Getter
    private String name;
}
