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

    public boolean updateFlightTracking(Long flightId, String action) {
        Optional<Flight> flight = flightRepository.findById(flightId);

        final OffsetDateTime currentTime = OffsetDateTime.now();

        if (!flight.isPresent()) {
            return false;
        }

        if (action.equals(FlightAction.START)) {
            flight.get().setActualStartTime(currentTime);
            flightRepository.save(flight.get());
        }
        else if (action.equals(FlightAction.LANDING)) {
            flight.get().setActualLandingTime(currentTime);
            flightRepository.save(flight.get());
        }

        return true;
    }
}
