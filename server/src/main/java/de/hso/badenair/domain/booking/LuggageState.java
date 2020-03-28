package de.hso.badenair.domain.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum LuggageState {
    ON_PLANE("Im Flugzeug"),
    READY_FOR_PICK_UP("Abholbereit");

    @Getter
    private String name;
}
