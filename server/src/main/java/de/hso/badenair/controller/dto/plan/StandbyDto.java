package de.hso.badenair.controller.dto.plan;

import lombok.Value;

import java.time.OffsetDateTime;

@Value
public class StandbyDto {
    String employee;
    OffsetDateTime startTime;
    OffsetDateTime endTime;
}
