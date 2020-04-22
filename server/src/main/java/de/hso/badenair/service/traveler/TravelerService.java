package de.hso.badenair.service.traveler;


import de.hso.badenair.domain.booking.Traveler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TravelerService {
    private final TravelerRepository travelerRepository;

    public Optional<Traveler> getTravelerById(Long TravelerId){
        return travelerRepository.findById(TravelerId);
    }

    public boolean updateCheckIn(Long TravelerId, Integer seatNumber){
        Optional<Traveler> traveler = travelerRepository.findById(TravelerId);

        if (!traveler.isPresent()) {
            return false;
        }

        Traveler realTaveler = traveler.get();
        realTaveler.setCheckedIn(true);
        realTaveler.setSeatNumber(seatNumber);

        // Update state
        travelerRepository.save(traveler.get());

        return true;
    }

}
