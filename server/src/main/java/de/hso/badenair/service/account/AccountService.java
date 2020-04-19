package de.hso.badenair.service.account;

import de.hso.badenair.controller.dto.account.AccountDataDto;
import de.hso.badenair.controller.dto.account.FinishRegistrationDto;
import de.hso.badenair.domain.booking.AccountData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountDataRepository accountDataRepository;

    public AccountDataDto getAccountData(String userId) {
        return accountDataRepository.findByCustomerUserId(userId)
            .map(this::mapToDto)
            .orElse(AccountDataDto.builder().build());
    }

    @Transactional
    public void finishRegistration(String userId, FinishRegistrationDto dto) {
        final AccountData accountData = accountDataRepository.findByCustomerUserId(userId)
            .orElseGet(this::getEmptyAccountData);

        accountData.setBirthday(dto.getBirthday());
        accountData.setStreet(dto.getStreet());
        accountData.setZipCode(dto.getZipCode());
        accountData.setCity(dto.getPlaceOfResidence());
        accountData.setCardOwner(dto.getCardOwner());
        accountData.setCardNumber(dto.getCardNumber());
        accountData.setCvv(dto.getCheck());
        accountData.setExpirationDate(dto.getExpirationDate());
    }

    private AccountData getEmptyAccountData() {
        return accountDataRepository.save(AccountData.builder().build());
    }

    private AccountDataDto mapToDto(AccountData accountData) {
        return AccountDataDto.builder()
            .birthday(accountData.getBirthday())
            .street(accountData.getStreet())
            .zipCode(accountData.getZipCode())
            .placeOfResidence(accountData.getCity())
            .cardOwner(accountData.getCardOwner())
            .cardNumber(accountData.getCardNumber())
            .check(accountData.getCvv())
            .expirationDate(accountData.getExpirationDate())
            .build();
    }
}
