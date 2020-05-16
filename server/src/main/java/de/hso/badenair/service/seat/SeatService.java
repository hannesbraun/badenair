package de.hso.badenair.service.seat;

import de.hso.badenair.controller.dto.seat.SeatDto;
import de.hso.badenair.domain.booking.Booking;
import de.hso.badenair.domain.flight.Flight;
import de.hso.badenair.domain.plane.PlaneTypeData;
import de.hso.badenair.service.flight.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeatService {

    private final FlightRepository flightRepository;

    public SeatDto getSeatsByFlightId(long id) throws Exception {
        Flight currentFlight = flightRepository.findById(id).orElseThrow();


        List<Seat> seats = currentFlight.getBookings().stream()
            .map(Booking::getTravelers)
            .flatMap(Collection::stream)
            .map(traveler -> new Seat(
                traveler.getSeatColumn(),
                traveler.getSeatRow()))
            .collect(Collectors.toList());

        PlaneTypeData planeData = currentFlight.getPlane().getTypeData();
        int rows = planeData.getNumberOfRows();
        int columns = planeData.getNumberOfColumns();

        return new SeatDto(
            planeData.getType().toString(),
            getSeatMap(rows, columns, seats)
        );
    }

    private Boolean[][] getSeatMap(int rows, int columns, List<Seat> takenSeats) {
        Boolean[][] seatMap = new Boolean[rows][columns];

        // TODO: add only available Seats
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                seatMap[row][column] = !takenSeats.contains(new Seat(column, row));
            }
        }

        return seatMap;
    }
}

@Value
class Seat {
    int column;
    int row;
}
