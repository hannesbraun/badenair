package de.hso.badenair.controller.dto.traveler;

import lombok.Value;

@Value
public class CheckInTravelerDto {
    Long id;
    String name;
    String surname;
    boolean checkedIn;
    int baggage1;
    int baggage2;
    int baggage3;
    int baggage4;
    int seatRow;
    int seatColumn;
}
