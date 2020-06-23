package de.hso.badenair.service.seat;

import de.hso.badenair.controller.dto.seat.SeatDto;
import de.hso.badenair.controller.dto.seat.SelectedSeatDto;
import de.hso.badenair.domain.booking.Booking;
import de.hso.badenair.domain.flight.Flight;
import de.hso.badenair.domain.plane.PlaneTypeData;
import de.hso.badenair.service.flight.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final FlightRepository flightRepository;

    public SeatDto getSeatsByFlightId(long id) {
        Flight currentFlight = flightRepository.findById(id).orElseThrow();
        List<SelectedSeatDto> seats = this.getSelectedSeats(currentFlight);

        PlaneTypeData planeData = currentFlight.getPlane().getTypeData();
        int rows = planeData.getNumberOfRows();
        int columns = planeData.getNumberOfColumns();

        return new SeatDto(
            planeData.getType().toString(),
            getSeatMap(rows, columns, seats)
        );
    }

    public boolean isSeatTaken(Flight flight, SelectedSeatDto seat) {
        List<SelectedSeatDto> seats = this.getSelectedSeats(flight);
        return seats.contains(seat);
    }

    private List<SelectedSeatDto> getSelectedSeats(Flight flight) {
        return flight.getBookings().stream()
            .map(Booking::getTravelers)
            .flatMap(Collection::stream)
            .map(traveler -> new SelectedSeatDto(
                traveler.getSeatRow(),
                traveler.getSeatColumn()))
            .collect(Collectors.toList());
    }

    private Boolean[][] getSeatMap(int rows, int columns, List<SelectedSeatDto> takenSeats) {
        Boolean[][] seatMap = new Boolean[rows][columns];

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                seatMap[row][column] = !takenSeats.contains(new SelectedSeatDto(row, column));
            }
        }
        return seatMap;
    }
}
