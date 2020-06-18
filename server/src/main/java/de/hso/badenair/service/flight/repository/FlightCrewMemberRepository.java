package de.hso.badenair.service.flight.repository;

import de.hso.badenair.domain.flight.FlightCrewMember;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightCrewMemberRepository extends CrudRepository<FlightCrewMember, Long> {
}
