package de.hso.badenair.domain.plane;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum PlaneState {
    WAITING("Wartend"),
    ON_FLIGHT("Im Flug"),
    IN_MAINTENANCE("In Wartung");

    @Getter
    private String name;
}
