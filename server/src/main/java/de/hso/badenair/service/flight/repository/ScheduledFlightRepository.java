package de.hso.badenair.service.flight.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.hso.badenair.domain.flight.ScheduledFlight;

import java.util.List;

@Repository
public interface ScheduledFlightRepository extends CrudRepository<ScheduledFlight, Long> {
    List<ScheduledFlight> findByStartingAirportId(long startingAirportId);
    List<ScheduledFlight> findByStartingAirportIdAndDestinationAirportId(long startingAirportId, long destinationAirportId);
}
