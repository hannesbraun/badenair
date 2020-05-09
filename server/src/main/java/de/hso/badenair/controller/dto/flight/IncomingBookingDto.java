package de.hso.badenair.controller.dto.flight;

import de.hso.badenair.controller.dto.seat.SelectedSeatDto;
import de.hso.badenair.controller.dto.traveler.IncomingTravelerDto;
import lombok.Value;

@Value
public class IncomingBookingDto {
    Long flightId;
    IncomingTravelerDto[] passengers;
    SelectedSeatDto[] seats;
    double price;


}
