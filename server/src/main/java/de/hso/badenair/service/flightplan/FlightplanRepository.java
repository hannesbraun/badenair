package de.hso.badenair.service.flightplan;

import de.hso.badenair.domain.flight.Flight;
import de.hso.badenair.domain.plane.Plane;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface FlightplanRepository extends CrudRepository<Flight, Long> {
    List<Flight> findByStartDateBetweenAndPlane_IdEquals(OffsetDateTime start, OffsetDateTime end, Long id);
}
