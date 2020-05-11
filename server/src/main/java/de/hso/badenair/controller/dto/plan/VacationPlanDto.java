package de.hso.badenair.controller.dto.plan;

import lombok.Value;

import java.time.OffsetDateTime;
import java.util.List;

@Value
public class VacationPlanDto {
    List<VacationDto> vacations;
    Integer remainingVacationDays;
}
