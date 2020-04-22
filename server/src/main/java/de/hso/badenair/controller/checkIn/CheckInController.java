package de.hso.badenair.controller.checkIn;


import de.hso.badenair.service.traveler.TravelerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/flight")
@RequiredArgsConstructor
public class CheckInController {

    private final TravelerService travelerService;

    @PatchMapping("/checkin/{id}")
    public ResponseEntity<Object> updateCheckIn(@PathVariable long id, @RequestBody Integer seatNumber) {
        boolean updateSuccess = travelerService.updateCheckIn(id, seatNumber);

        if (updateSuccess) {
            return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
        } else {
            // The only possible failure is probably that the luggage id isn't
            // present in the database
            return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
        }
    }
}
