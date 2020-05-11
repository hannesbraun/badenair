package de.hso.badenair.service.traveler;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.hso.badenair.domain.booking.Traveler;

@Repository
public interface TravelerRepository extends CrudRepository<Traveler, Long> {
}
