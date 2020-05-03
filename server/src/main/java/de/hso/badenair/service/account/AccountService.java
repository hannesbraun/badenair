package de.hso.badenair.service.account;

import de.hso.badenair.controller.dto.account.AccountDataDto;
import de.hso.badenair.controller.dto.account.UpdateAccountDataDto;
import de.hso.badenair.domain.booking.account.AccountData;
import de.hso.badenair.util.mapper.AccountDataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountDataRepository accountDataRepository;

    public AccountDataDto getAccountData(String userId) {
        return accountDataRepository.findByCustomerUserId(userId)
            .map(AccountDataMapper::mapToDto)
            .orElse(AccountDataDto.builder().build());
    }

    @Transactional
    public void updateAccountData(String userId, UpdateAccountDataDto dto) {
        final AccountData accountData = accountDataRepository.findByCustomerUserId(userId)
            .orElseGet(this::getEmptyAccountData);

        accountData.setCustomerUserId(userId);
        accountData.setBirthday(dto.getBirthDate());
        accountData.setStreet(dto.getStreet());
        accountData.setZipCode(dto.getZipCode());
        accountData.setCity(dto.getPlaceOfResidence());
        accountData.setCardOwner(dto.getCardOwner());
        accountData.setCardNumber(dto.getCardNumber());
        accountData.setCvv(dto.getCheck());
        accountData.setExpirationDate(dto.getInvalidationDate());
    }

    private AccountData getEmptyAccountData() {
        return accountDataRepository.save(AccountData.builder().build());
    }
}
