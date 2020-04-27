package de.hso.badenair.controller.dto.plan;

import lombok.Value;

import javax.validation.constraints.Future;
import java.time.OffsetDateTime;

@Value
public class RequestVacationDto {

    @Future
    OffsetDateTime startDate;

    @Future
    OffsetDateTime endDate;
}
