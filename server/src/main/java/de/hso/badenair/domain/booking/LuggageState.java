package de.hso.badenair.domain.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum LuggageState {
	AT_TRAVELLER("Beim Reisenden"),
	ON_BAGGAGE_CAROUSEL("Auf dem Gepäckband"),
	IN_LUGGAGE_HALL("In der Gepäckhalle"),
	ON_LUGGAGE_CART("Auf dem Gepäckwagen"),
	ON_PLANE("Im Flugzeug"),
	READY_FOR_PICK_UP("Bereit zum Abholen");

	@Getter
	private String name;
}
