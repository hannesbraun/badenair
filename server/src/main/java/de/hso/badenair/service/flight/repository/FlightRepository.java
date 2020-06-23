package de.hso.badenair.service.flight.repository;

import java.time.OffsetDateTime;
import java.util.List;

import de.hso.badenair.domain.flight.Flight;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends CrudRepository<Flight, Long> {
	  List<Flight> findByActualLandingTimeBetween(OffsetDateTime start, OffsetDateTime end);
    List<Flight> findByScheduledFlightId(long scheduledFlightId);
}