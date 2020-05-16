package de.hso.badenair.service.account;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import de.hso.badenair.controller.dto.account.AccountDataDto;
import de.hso.badenair.controller.dto.account.UpdateAccountDataDto;
import de.hso.badenair.domain.booking.Booking;
import de.hso.badenair.domain.booking.account.AccountData;
import de.hso.badenair.service.booking.repository.BookingRepository;
import de.hso.badenair.util.mapper.AccountDataMapper;
import de.hso.badenair.util.time.DateFusioner;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

	private final AccountDataRepository accountDataRepository;

	private final BookingRepository bookingRepository;

	public AccountDataDto getAccountData(String userId) {
		return accountDataRepository.findByCustomerUserId(userId)
				.map(AccountDataMapper::mapToDto)
				.orElse(AccountDataDto.builder().build());
	}

	@Transactional
	public void updateAccountData(String userId, UpdateAccountDataDto dto) {
		final AccountData accountData = accountDataRepository
				.findByCustomerUserId(userId)
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

	public List<Booking> getBookings(String customerUserId) {
		// Get bookings and filter out bookings of the past
		return bookingRepository.findAllByCustomerUserId(customerUserId)
				.stream()
				.filter(booking -> DateFusioner
						.fusionStartDate(booking.getFlight().getStartDate(),
								booking.getFlight().getScheduledFlight()
										.getStartTime(),
								null)
						.isAfter(OffsetDateTime.now()))
				.collect(Collectors.toList());
	}
}
