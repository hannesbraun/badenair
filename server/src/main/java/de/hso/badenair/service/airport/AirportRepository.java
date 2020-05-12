package de.hso.badenair.service.airport;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.hso.badenair.domain.flight.Airport;

@Repository
public interface AirportRepository extends CrudRepository<Airport, Long> {
	public Optional<Airport> findByName(String name);
}
