package de.hso.badenair.service.luggage;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.hso.badenair.domain.booking.Luggage;

@Repository
public interface LuggageRepository extends CrudRepository<Luggage, Long> {
}
