package de.hso.badenair.controller.dto.flight;

import lombok.Value;

import java.time.OffsetDateTime;

@Value
public class TrackingDto {
    String action;
    double delay;
    OffsetDateTime date;
}
