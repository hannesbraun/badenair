package de.hso.badenair.controller.boardingpass;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.hso.badenair.service.boardingpass.BoardingPassService;
import lombok.RequiredArgsConstructor;

/**
 * This controller is responsible for retrieving the boarding pass pdf document for a traveler.
 */
@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class BoardingPassController {

	private final BoardingPassService boardingPassService;

	/**
	 * Retrieves the boarding pass for the given traveler id.
	 * @param travelerId the (internal) id of the traveler of which to retrieve the boarding pass from
	 * @return Returns http status code 404 if the traveler was not found.
	 * Else, the pdf document will be returned alongside with http status code 200.
	 */
	@GetMapping("/boardingpass")
	public ResponseEntity<?> getBoardingPass(@RequestParam Long travelerId) {
		final byte[] boardingPassPdf = boardingPassService
				.getBoardingPass(travelerId);

		if (boardingPassPdf == null) {
			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
		} else {
			return ResponseEntity.ok(boardingPassPdf);
		}
	}
}
