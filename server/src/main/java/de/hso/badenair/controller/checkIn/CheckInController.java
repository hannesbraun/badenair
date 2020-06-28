package de.hso.badenair.controller.checkIn;


import de.hso.badenair.controller.dto.checkin.CheckInInfoDto;
import de.hso.badenair.service.traveler.TravelerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/customer/flight/checkin")
@RequiredArgsConstructor
public class CheckInController {

    private final TravelerService travelerService;

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateCheckIn(@PathVariable long id) {
        boolean updateSuccess = travelerService.updateCheckIn(id);

        if (updateSuccess) {
            return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
        } else {
            // The only possible failure is probably that the luggage id isn't
            // present in the database
            return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{flightId}")
    public ResponseEntity<CheckInInfoDto> getCheckInInfo(Principal user, @PathVariable Long flightId) {
        final CheckInInfoDto dto = travelerService.getCheckInInfo(user.getName(), flightId);
        return ResponseEntity.ok(dto);
    }
}
