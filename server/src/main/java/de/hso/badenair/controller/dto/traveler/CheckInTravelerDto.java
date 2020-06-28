package de.hso.badenair.controller.dto.traveler;

import de.hso.badenair.controller.dto.luggage.LuggageDto;
import lombok.Value;

@Value
public class CheckInTravelerDto {
    Long id;
    String name;
    String surname;
    boolean checkedIn;
    LuggageDto[] baggage;
    int seatRow;
    int seatColumn;
}
