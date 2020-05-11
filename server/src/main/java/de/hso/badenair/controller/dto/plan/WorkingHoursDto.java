package de.hso.badenair.controller.dto.plan;

import lombok.Value;

import java.time.OffsetDateTime;

@Value
public class WorkingHoursDto {
    OffsetDateTime startTime;
    OffsetDateTime endTime;
}
