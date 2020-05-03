package de.hso.badenair.controller.dto.flightplan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
@Builder
@AllArgsConstructor
public class PlaneScheduleDto {
    private long id;
    private String plane;
    private String status;
    private boolean hasConflict;
    private ArrayList<FlightDto> flights;
}