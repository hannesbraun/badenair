package de.hso.badenair.service.traveler;


import de.hso.badenair.domain.booking.Traveler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Repository
@Service
@RequiredArgsConstructor
public class TravelerService {
    private final TravelerRepository travelerRepository;

    public boolean updateCheckIn(Long TravelerId){
        Optional<Traveler> traveler = travelerRepository.findById(TravelerId);

        if (!traveler.isPresent()) {
            return false;
        }

        Traveler realTraveler = traveler.get();
        realTraveler.setCheckedIn(true);

        // Update state
        travelerRepository.save(traveler.get());

        return true;
    }

}
