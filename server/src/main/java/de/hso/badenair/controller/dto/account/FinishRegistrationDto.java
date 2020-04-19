package de.hso.badenair.controller.dto.account;

import lombok.Value;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.OffsetDateTime;

@Value
public class FinishRegistrationDto {
    @Past
    OffsetDateTime birthDate;

    @NotBlank
    String street;

    @NotBlank
    String zipCode;

    @NotBlank
    String placeOfResidence;

    @NotBlank
    String cardOwner;

    @NotBlank
    String cardNumber;

    @NotBlank
    String check;

    @Future
    OffsetDateTime invalidationDate;
}
