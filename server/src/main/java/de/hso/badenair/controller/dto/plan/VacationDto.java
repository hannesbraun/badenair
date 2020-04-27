package de.hso.badenair.controller.dto.plan;

import lombok.Value;

import java.time.OffsetDateTime;

@Value
public class VacationDto {
    OffsetDateTime startDate;
    OffsetDateTime endDate;
    // TODO: Add state
}
