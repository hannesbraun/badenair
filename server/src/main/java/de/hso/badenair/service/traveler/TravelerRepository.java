package de.hso.badenair.service.traveler;

import de.hso.badenair.domain.booking.Traveler;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelerRepository extends CrudRepository<Traveler, Long> {

}
