package de.hso.badenair.service.flight.repository;

import java.time.OffsetDateTime;
import java.util.List;

import de.hso.badenair.domain.flight.Flight;
import de.hso.badenair.domain.plane.Plane;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends CrudRepository<Flight, Long> {
	  List<Flight> findByActualLandingTimeBetween(OffsetDateTime start, OffsetDateTime end);
	  List<Flight> findByPlaneAndStartDateBetween(Plane plane, OffsetDateTime start, OffsetDateTime end );
	  List<Flight> findByScheduledFlightId(long scheduledFlightId);
}
