package de.hso.badenair.controller.dto.account;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class AccountDataDto {
    private OffsetDateTime birthday;
    private String street;
    private String zipCode;
    private String placeOfResidence;
    private String cardOwner;
    private String cardNumber;
    private String check;
    private OffsetDateTime expirationDate;
}
