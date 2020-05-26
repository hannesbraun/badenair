package de.hso.badenair.service.booking.repository;

import de.hso.badenair.domain.booking.Booking;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends CrudRepository<Booking, Long> {
    List<Booking> findAllByCustomerUserId(String customerUserId);
	Optional<Booking> findByCustomerUserIdAndFlight_IdEquals(String customerUserId, Long id);
}
