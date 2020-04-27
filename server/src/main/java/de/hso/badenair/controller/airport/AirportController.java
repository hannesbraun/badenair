package de.hso.badenair.controller.airport;

import de.hso.badenair.controller.dto.airport.AirportDto;
import de.hso.badenair.service.airport.AirportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customer/public")
@RequiredArgsConstructor
public class AirportController {
    private final AirportService airportService;

    @GetMapping("/airport")
    public ResponseEntity<List<AirportDto>> getAirports() {
        return ResponseEntity.ok(
            this.airportService.getAirports().stream()
                .map(airport -> new AirportDto(
                    airport.getId(),
                    airport.getName()
                ))
                .collect(Collectors.toList())
        );
    }
}
