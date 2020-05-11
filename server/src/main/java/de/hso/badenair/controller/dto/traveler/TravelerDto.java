package de.hso.badenair.controller.dto.traveler;

import lombok.Value;

@Value
public class TravelerDto {
	public String name;

	public Long id;

	public boolean checkedIn;
}
