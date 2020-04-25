package de.hso.badenair.service.airport;

import de.hso.badenair.domain.flight.Airport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AirportService {

    final private AirportRepository airportRepository;

    public List<Airport> getAirports() {
        return (List<Airport>) this.airportRepository.findAll();
    }
}
