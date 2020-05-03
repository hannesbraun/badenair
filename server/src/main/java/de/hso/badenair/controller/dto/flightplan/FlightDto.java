package de.hso.badenair.controller.dto.flightplan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
public class FlightDto {
    private long id;
    private String start;
    private String destination;
    private OffsetDateTime startTime;
    private OffsetDateTime arrivalTime;
}