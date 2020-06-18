package de.hso.badenair.service.flight.repository;

import de.hso.badenair.domain.flight.Flight;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightRepository extends CrudRepository<Flight, Long> {
    List<Flight> findByScheduledFlightId(long scheduledFlightId);
}
