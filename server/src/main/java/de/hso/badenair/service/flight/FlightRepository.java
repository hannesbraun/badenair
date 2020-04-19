package de.hso.badenair.service.flight;

import de.hso.badenair.domain.flight.Flight;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends CrudRepository<Flight, Long> {

}
