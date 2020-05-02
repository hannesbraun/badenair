package de.hso.badenair.controller.dto.seat;

import lombok.Value;

@Value
public class SeatDto {
    String type;
    Integer row;
    Integer column;
    // TODO: add if seat is free
}
