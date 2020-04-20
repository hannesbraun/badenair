package de.hso.badenair.service.traveler;

import org.springframework.data.repository.CrudRepository;

import de.hso.badenair.domain.booking.Traveler;

public interface TravelerRepository extends CrudRepository<Traveler, Long> {
}
