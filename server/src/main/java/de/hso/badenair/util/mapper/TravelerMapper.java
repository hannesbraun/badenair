package de.hso.badenair.util.mapper;

import de.hso.badenair.controller.dto.traveler.CheckInTravelerDto;
import de.hso.badenair.domain.booking.Luggage;
import de.hso.badenair.domain.booking.Traveler;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class TravelerMapper {

    /**
     * @param traveler Traveler to map to the corresponding dto
     * @return Returns the mapped dto
     */

    public static CheckInTravelerDto mapToDto(Traveler traveler) {
        final List<Integer> luggage = traveler.getLuggage().stream()
            .map(Luggage::getWeight)
            .collect(Collectors.toList());

        if (luggage.size() < 4) {
            final List<Integer> pad = Collections.nCopies(4 - luggage.size(), 0);
            luggage.addAll(pad);
        }

        return new CheckInTravelerDto(
            traveler.getId(),
            traveler.getFirstName(),
            traveler.getLastName(),
            traveler.isCheckedIn(),
            luggage.get(0),
            luggage.get(1),
            luggage.get(2),
            luggage.get(3),
            traveler.getSeatRow(),
            traveler.getSeatColumn()
        );
    }
}
