package de.hso.badenair.controller.dto.flightplan;

import lombok.Value;

import java.time.OffsetDateTime;

@Value
public class FlightWithoutPriceDto {
    long id;
    String start;
    String destination;
    OffsetDateTime startTime;
    OffsetDateTime arrivalTime;
    OffsetDateTime realStartTime;
    OffsetDateTime realLandingTime;
}
