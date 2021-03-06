package de.hso.badenair.service.traveler;


import de.hso.badenair.controller.dto.checkin.CheckInInfoDto;
import de.hso.badenair.controller.dto.traveler.CheckInTravelerDto;
import de.hso.badenair.domain.booking.Booking;
import de.hso.badenair.domain.booking.Traveler;
import de.hso.badenair.domain.flight.Flight;
import de.hso.badenair.service.booking.repository.BookingRepository;
import de.hso.badenair.util.mapper.FlightMapper;
import de.hso.badenair.util.mapper.TravelerMapper;
import de.hso.badenair.util.time.DateFusioner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TravelerService {
    private static final int CHECK_IN_TIME_LIMIT = 30;
    private final TravelerRepository travelerRepository;
    private final BookingRepository bookingRepository;

    /**
     * @param TravelerId ID of the traveler that is updated
     * @return Returns whether the update was successful
     */
    @Transactional
    public boolean updateCheckIn(Long TravelerId) {
        Optional<Traveler> traveler = travelerRepository.findById(TravelerId);

        if (traveler.isEmpty()) {
            return false;
        }

        Traveler realTraveler = traveler.get();
        if (!isBeforeCheckInTimeLimit(realTraveler.getBooking().getFlight())) {
            return false;
        }

        realTraveler.setCheckedIn(true);

        return true;
    }

    /**
     * @param customerUserId ID to identify the customer
     * @param flightId ID of the flight to get the info from
     * @return Returns All data needed for the check in
     */
    public CheckInInfoDto getCheckInInfo(String customerUserId, Long flightId) {
        final Booking booking = bookingRepository.findByCustomerUserIdAndFlight_IdEquals(customerUserId, flightId)
            .orElseThrow();

        final List<CheckInTravelerDto> travelerDtos = booking.getTravelers().stream()
            .map(TravelerMapper::mapToDto)
            .collect(Collectors.toList());
        return new CheckInInfoDto(travelerDtos, FlightMapper.mapToDto(booking.getFlight()));
    }

    private boolean isBeforeCheckInTimeLimit(Flight flight) {
        OffsetDateTime startDate = DateFusioner.fusionStartDate(
            flight.getStartDate(),
            flight.getScheduledFlight().getStartTime(), null);

        return OffsetDateTime.now().withOffsetSameLocal(ZoneOffset.of("+1"))
            .isBefore(startDate.minusMinutes(CHECK_IN_TIME_LIMIT));
    }
}
