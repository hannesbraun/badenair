package de.hso.badenair.service.flight.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.hso.badenair.domain.flight.ScheduledFlight;

@Repository
public interface ScheduledFlightRepository
		extends
			CrudRepository<ScheduledFlight, Long> {

}
