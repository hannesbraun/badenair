package de.hso.badenair.service.account;

import de.hso.badenair.domain.booking.AccountData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountDataRepository extends CrudRepository<AccountData, Long> {
    Optional<AccountData> findByCustomerUserId(String userId);
}
