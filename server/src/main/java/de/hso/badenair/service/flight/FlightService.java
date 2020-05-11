package de.hso.badenair.service.flight;

import de.hso.badenair.domain.flight.Flight;
import de.hso.badenair.domain.flight.FlightAction;
import de.hso.badenair.service.flight.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FlightService {

	private final FlightRepository flightRepository;

    public OffsetDateTime updateFlightTracking(Long flightId, String action) {
        Optional<Flight> flight = flightRepository.findById(flightId);

		final OffsetDateTime currentTime = OffsetDateTime.now();

        if (!flight.isPresent()) {
            return null;
        }

        if (action.equals(FlightAction.START.getName())) {
            flight.get().setActualStartTime(currentTime);
            flightRepository.save(flight.get());
        } else if (action.equals(FlightAction.LANDING.getName())) {
            flight.get().setActualLandingTime(currentTime);
            flightRepository.save(flight.get());
        } else {
            return null;
        }

        return currentTime;
    }

    public Flight getFlightById(Long id){
        Optional<Flight> flight = flightRepository.findById(id);

        if (!flight.isPresent()) {
            return null;
        }

        return flight.get();
    }
}
