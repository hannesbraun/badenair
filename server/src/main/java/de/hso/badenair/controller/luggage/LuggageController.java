package de.hso.badenair.controller.luggage;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.hso.badenair.controller.dto.luggage.LuggageStateDto;
import de.hso.badenair.service.luggage.LuggageService;
import lombok.RequiredArgsConstructor;

/**
 * Controller for updating the luggage state.
 */
@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class LuggageController {

	private final LuggageService luggageService;

	/**
	 * Updates the state of the given piece of luggage.
	 * @param dto the data sent by the client
	 * @return If the update was successful, HTTP Status 204 will be returned. Else, an error 404 will be returned.
	 */
	@PatchMapping("/luggage")
	public ResponseEntity<Object> updateLuggageState(
			@RequestBody @Valid LuggageStateDto dto) {
		boolean updateSuccess = luggageService.updateLuggageState(dto.getId(),
				dto.getState());

		if (updateSuccess) {
			return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
		} else {
			// The only possible failure is probably that the luggage id isn't
			// present in the database
			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
		}
	}

}
