package de.hso.badenair.controller.flight.search;

import de.hso.badenair.domain.flight.Flight;
import de.hso.badenair.service.flight.search.FlightSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/customer/flight")
@RequiredArgsConstructor
public class FlightSearchController {
    private final FlightSearchService flightService;

    @GetMapping("/search")
    public ResponseEntity<List<Flight>> getFlights(
        @RequestParam int start,
        @RequestParam int destination,
        @RequestParam int passengers,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
        return ResponseEntity.ok(this.flightService.getFlights(start, destination, passengers, date));
    }
}
