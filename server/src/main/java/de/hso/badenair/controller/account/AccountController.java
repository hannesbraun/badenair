package de.hso.badenair.controller.account;

import de.hso.badenair.controller.dto.account.AccountDataDto;
import de.hso.badenair.controller.dto.account.UpdateAccountDataDto;
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
    public ResponseEntity<?> updateAccountData(Principal user, @RequestBody @Valid UpdateAccountDataDto dto) {
        accountService.updateAccountData(user.getName(), dto);
        return ResponseEntity.ok().build();
    }
}
