package de.hso.badenair.controller.boardingpass;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.hso.badenair.service.boardingpass.BoardingPassService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class BoardingPassController {

	private final BoardingPassService boardingPassService;

	@GetMapping("/boardingpass")
	public ResponseEntity<?> getBoardingPass(Long travelerId) {
		final byte[] boardingPassPdf = boardingPassService
				.getBoardingPass(travelerId);

		if (boardingPassPdf == null) {
			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
		} else {
			return ResponseEntity.ok(boardingPassPdf);
		}
	}
}
