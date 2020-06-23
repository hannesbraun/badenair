package de.hso.badenair.controller.dto.luggage;

import de.hso.badenair.domain.booking.LuggageState;
import lombok.Value;

@Value
public class LuggageDto {
    Integer weight;
    LuggageState state;
}
