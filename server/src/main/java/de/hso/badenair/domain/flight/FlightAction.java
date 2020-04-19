package de.hso.badenair.domain.flight;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum FlightAction {
    START("Start"),
    LANDING("Landung");

    @Getter
    private String name;
}
