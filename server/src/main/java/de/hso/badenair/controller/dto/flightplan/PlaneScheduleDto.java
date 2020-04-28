package de.hso.badenair.controller.dto.flightplan;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class PlaneScheduleDto {
    private long id;
    private String plane;
    private String status;
    private boolean hasConflict;
    private FlightDto[] flights;
}