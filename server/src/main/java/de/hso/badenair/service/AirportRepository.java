package de.hso.badenair.service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.hso.badenair.domain.flight.Airport;

@Repository
public interface AirportRepository extends CrudRepository<Airport, Long> {
}
