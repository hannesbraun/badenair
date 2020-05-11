package de.hso.badenair.controller.dto.booking;

import java.util.List;

import de.hso.badenair.controller.dto.flight.FlightDto;
import de.hso.badenair.controller.dto.luggage.LuggageStateDto;
import de.hso.badenair.controller.dto.traveler.TravelerDto;
import lombok.Value;

@Value
public class BookingDto {
	public FlightDto flight;

	public List<TravelerDto> travelers;

	public List<LuggageStateDto> luggage;
}
