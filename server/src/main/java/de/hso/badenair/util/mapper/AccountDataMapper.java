package de.hso.badenair.util.mapper;

import de.hso.badenair.controller.dto.account.AccountDataDto;
import de.hso.badenair.domain.booking.AccountData;

public abstract class AccountDataMapper {

    public static AccountDataDto mapToDto(AccountData accountData) {
        return AccountDataDto.builder()
            .birthDate(accountData.getBirthday())
            .street(accountData.getStreet())
            .zipCode(accountData.getZipCode())
            .placeOfResidence(accountData.getCity())
            .cardOwner(accountData.getCardOwner())
            .cardNumber(accountData.getCardNumber())
            .check(accountData.getCvv())
            .invalidationDate(accountData.getExpirationDate())
            .build();
    }
}
