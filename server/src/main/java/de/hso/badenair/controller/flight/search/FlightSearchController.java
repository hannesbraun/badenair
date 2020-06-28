package de.hso.badenair.controller.flight.search;

import de.hso.badenair.controller.dto.flight.FlightDto;
import de.hso.badenair.service.flight.search.FlightSearchService;
import de.hso.badenair.util.mapper.FlightMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customer/public/flight")
@RequiredArgsConstructor
public class FlightSearchController {
    private final FlightSearchService flightService;

    @GetMapping("/search")
    public ResponseEntity<List<FlightDto>> getFlights(@RequestParam int start,
                                                      @RequestParam int destination, @RequestParam int passengers,
                                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime date) {

        final List<FlightDto> flights = this.flightService
            .getFlights(start, destination, passengers, date).stream()
            .map(FlightMapper::mapToDto)
            .collect(Collectors.toList());

        return ResponseEntity.ok(flights);
    }
}
