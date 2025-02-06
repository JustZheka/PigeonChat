package com.pigeonchat.lab6.controllers;

import com.pigeonchat.lab6.dto.AccountDeleteRequestDTO;
import com.pigeonchat.lab6.dto.AccountRequestDTO;
import com.pigeonchat.lab6.dto.AccountResponseDTO;
import com.pigeonchat.lab6.dto.AccountUpdateRequestDTO;
import com.pigeonchat.lab6.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponseDTO> createAccount(@RequestBody AccountRequestDTO accountRequestDTO) {
        AccountResponseDTO responseDTO = accountService.createAccount(accountRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/{login}")
    public ResponseEntity<AccountResponseDTO> getAccountByLogin(@PathVariable String login) {
        AccountResponseDTO responseDTO = accountService.getAccountByLogin(login);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/changePassword")
    public ResponseEntity<Void> updatePassword(@RequestBody AccountUpdateRequestDTO accountUpdateRequestDTO) {
        accountService.updatePassword(accountUpdateRequestDTO.getLogin(), accountUpdateRequestDTO.getOldPassword(), accountUpdateRequestDTO.getNewPassword());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteAccount(@RequestBody AccountDeleteRequestDTO accountDeleteRequestDTO) {
        accountService.deleteAccount(accountDeleteRequestDTO.getLogin(), accountDeleteRequestDTO.getPassword());
        return ResponseEntity.noContent().build();
    }
}
