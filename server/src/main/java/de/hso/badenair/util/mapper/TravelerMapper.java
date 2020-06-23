package de.hso.badenair.util.mapper;

import de.hso.badenair.controller.dto.luggage.LuggageDto;
import de.hso.badenair.controller.dto.traveler.CheckInTravelerDto;
import de.hso.badenair.domain.booking.Traveler;

import java.util.List;
import java.util.stream.Collectors;

public abstract class TravelerMapper {

    /**
     * @param traveler Traveler to map to the corresponding dto
     * @return Returns the mapped dto
     */

    public static CheckInTravelerDto mapToDto(Traveler traveler) {
        final List<LuggageDto> luggageList = traveler.getLuggage().stream()
            .map(luggage -> new LuggageDto(luggage.getWeight(), luggage.getState()))
            .collect(Collectors.toList());

        LuggageDto[] luggageArray = new LuggageDto[luggageList.size()];

        return new CheckInTravelerDto(
            traveler.getId(),
            traveler.getFirstName(),
            traveler.getLastName(),
            traveler.isCheckedIn(),
            luggageList.toArray(luggageArray),
            traveler.getSeatRow(),
            traveler.getSeatColumn()
        );
    }
}
