package de.hso.badenair.controller.dto.plan;

import lombok.Value;

import java.time.OffsetDateTime;

@Value
public class ShiftDto {
    OffsetDateTime startTime;
    OffsetDateTime endTime;
}
