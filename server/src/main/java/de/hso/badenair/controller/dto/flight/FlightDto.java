package de.hso.badenair.controller.dto.flight;

import lombok.Value;

import java.time.OffsetDateTime;

@Value
public class FlightDto {
    long id;
    String start;
    String destination;
    OffsetDateTime startTime;
    OffsetDateTime arrivalTime;
    double price;
}
