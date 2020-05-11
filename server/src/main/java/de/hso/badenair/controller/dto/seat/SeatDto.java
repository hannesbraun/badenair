package de.hso.badenair.controller.dto.seat;

import lombok.Value;

@Value
public class SeatDto {
    String type;
    Boolean[][] freeSeats;
}
