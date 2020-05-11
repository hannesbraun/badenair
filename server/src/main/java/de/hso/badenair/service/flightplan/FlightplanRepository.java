package de.hso.badenair.service.flightplan;

import de.hso.badenair.domain.plane.Plane;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface FlightplanRepository extends CrudRepository<Plane, Long> {
    @Query("select p from Plane p inner join Flight f on p = f where f.actualStartTime between :date1 and :date2")
    List<Plane> findByActualStartTimeBetween(OffsetDateTime date1, OffsetDateTime date2);
}
