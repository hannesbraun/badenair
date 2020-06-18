package de.hso.badenair.domain.flight;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum FlightAction {
    START("Start"),
    LANDING("Landung"),
    DELAY("Verspätung"),
    STANDBY("Bereit");

    @Getter
    private String name;
}
