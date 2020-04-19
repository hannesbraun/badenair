package de.hso.badenair.controller.account;

import de.hso.badenair.controller.dto.account.AccountDataDto;
import de.hso.badenair.controller.dto.account.FinishRegistrationDto;
import de.hso.badenair.service.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/customer/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<AccountDataDto> getAccountData(Principal user) {
        return ResponseEntity.ok(accountService.getAccountData(user.getName()));
    }

    @PutMapping
    public ResponseEntity<?> finishRegistration(Principal user, @RequestBody @Valid FinishRegistrationDto dto) {
        accountService.finishRegistration(user.getName(), dto);
        return ResponseEntity.ok().build();
    }
}
