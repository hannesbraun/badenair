package de.hso.badenair.service.seat;

import de.hso.badenair.controller.dto.seat.SeatDto;
import de.hso.badenair.domain.flight.Flight;
import de.hso.badenair.domain.plane.PlaneTypeData;
import de.hso.badenair.service.flight.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeatService {

    private final FlightRepository flightRepository;

    public SeatDto getSeatsByFlightId(long id) throws Exception {
        Flight currentFlight = flightRepository.findById(id).orElseThrow();

        PlaneTypeData planeData = currentFlight.getPlane().getTypeData();
        int rows = planeData.getNumberOfRows();
        int columns = planeData.getNumberOfColumns();

        return new SeatDto(
            planeData.getType().toString(),
            getSeatMap(rows, columns)
        );
    }

    private Boolean[][] getSeatMap(int rows, int columns) {
        Boolean[][] seatMap = new Boolean[rows][columns];

        // TODO: add only available Seats
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                seatMap[row][column] = true;
            }
        }
        return seatMap;
    }
}
