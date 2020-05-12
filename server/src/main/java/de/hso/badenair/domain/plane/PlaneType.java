package de.hso.badenair.domain.plane;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum PlaneType {
	Dash_8_400("Dash-8-400"), Dash_8_200("Dash-8-200"), B737_400("B737-400");

	@Getter
	private String name;
}
