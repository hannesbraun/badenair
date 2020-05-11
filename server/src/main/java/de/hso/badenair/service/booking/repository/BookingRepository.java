package de.hso.badenair.service.booking.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.hso.badenair.domain.booking.Booking;

@Repository
public interface BookingRepository extends CrudRepository<Booking, Long> {
	public List<Booking> findAllByCustomerUserId(String customerUserId);
}
