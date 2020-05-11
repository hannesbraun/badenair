package de.hso.badenair.service.flight.booking;

import de.hso.badenair.controller.dto.flight.IncomingBookingDto;
import de.hso.badenair.controller.dto.seat.SelectedSeatDto;
import de.hso.badenair.controller.dto.traveler.IncomingTravelerDto;
import de.hso.badenair.domain.booking.Booking;
import de.hso.badenair.domain.booking.Luggage;
import de.hso.badenair.domain.booking.LuggageState;
import de.hso.badenair.domain.booking.Traveler;
import de.hso.badenair.domain.flight.Flight;
import de.hso.badenair.service.booking.repository.BookingRepository;
import de.hso.badenair.service.flight.FlightService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final FlightService flightService;

    public boolean bookFlight(String username, IncomingBookingDto dto){
        Flight flight = flightService.getFlightById(dto.getFlightId());
        if(flight != null){
            Set<Traveler> travelers = new HashSet<>();
            Booking newBooking = Booking.builder()
                .flight(flight)
                .customerUserId(username)
                .price(dto.getPrice())
                .build();

            for (int i = 0; i < dto.getPassengers().length; i++ ){
                IncomingTravelerDto travelerDto = dto.getPassengers()[i];
                SelectedSeatDto selectedSeat = dto.getSeats()[i];
                Traveler traveler = Traveler.builder()
                    .firstName(travelerDto.getName())
                    .lastName(travelerDto.getSurname())
                    .checkedIn(travelerDto.isCheckedIn())
                    .booking(newBooking)
                    .seatNumber(""+ selectedSeat.getRow() + Character.toString((char)(65+ selectedSeat.getColumn())))
                    .build();
                travelers.add(traveler);

                Set<Luggage> luggages = new HashSet<>();
                if(travelerDto.getBaggage1() != 0) {
                    Luggage luggage1 = Luggage.builder()
                        .state(LuggageState.AT_TRAVELLER)
                        .weight(travelerDto.getBaggage1())
                        .traveler(traveler).build();
                    luggages.add(luggage1);
                }

                if(travelerDto.getBaggage2() != 0) {
                    Luggage luggage2 = Luggage.builder()
                        .state(LuggageState.AT_TRAVELLER)
                        .weight(travelerDto.getBaggage2())
                        .traveler(traveler).build();
                    luggages.add(luggage2);
                }
                if(travelerDto.getBaggage3() !=0) {
                    Luggage luggage3 = Luggage.builder()
                        .state(LuggageState.AT_TRAVELLER)
                        .weight(travelerDto.getBaggage3())
                        .traveler(traveler).build();
                    luggages.add(luggage3);
                }
                if(travelerDto.getBaggage4() !=0) {
                    Luggage luggage4 = Luggage.builder()
                        .state(LuggageState.AT_TRAVELLER)
                        .weight(travelerDto.getBaggage4())
                        .traveler(traveler).build();
                    luggages.add(luggage4);
                }
                if(!luggages.isEmpty()){
                    traveler.setLuggage(luggages);
                }
            }
            if(!travelers.isEmpty()){
                newBooking.setTravelers(travelers);
                bookingRepository.save(newBooking);
                return true;
            }
        }

        return false;
    }
}
