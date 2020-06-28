package de.hso.badenair.controller.dto.flightplan;

import lombok.Value;

import java.util.List;

@Value
public class ConflictDto {
    long flightID;
    boolean flightDelayed;
    boolean notEnoughPersonal;
    boolean planeNotAvailable;
    boolean planeNotAvailableFixable;
    List<PlaneDto> reservePlanes;
}
