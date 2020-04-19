package de.hso.badenair.service.flight.search;

import de.hso.badenair.domain.flight.Flight;
import de.hso.badenair.service.flight.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlightSearchService {
    private final FlightRepository flightRepository;

    public List<Flight> getFlights(int start, int destination, int passengers, Date date) {
        List<Flight> flights = (List<Flight>)flightRepository.findAll();
        return flights.stream()
            .filter(flight -> flight.getScheduledFlight().getDestinationAirport().getId() == start)
            .filter(flight -> flight.getScheduledFlight().getDestinationAirport().getId() == destination)
            .collect(Collectors.toList());
    }
}
