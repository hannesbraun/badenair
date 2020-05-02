package de.hso.badenair.service.seat;

import de.hso.badenair.controller.dto.seat.SeatDto;
import de.hso.badenair.service.flight.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeatService {

    private final FlightRepository flightRepository;

    public List<SeatDto> getSeatsByFlightId(long id) {
        return flightRepository.findById(id).stream()
            .map(flight -> flight.getPlane().getTypeData())
            .map(plane -> new SeatDto(
                plane.getType().getName(),
                plane.getNumberOfRows(),
                plane.getNumberOfColumns()
            ))
            .collect(Collectors.toList());
    }
}
