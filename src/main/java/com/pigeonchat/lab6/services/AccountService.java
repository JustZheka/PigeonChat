package com.pigeonchat.lab6.services;

import com.pigeonchat.lab6.dto.AccountRequestDTO;
import com.pigeonchat.lab6.dto.AccountResponseDTO;
import com.pigeonchat.lab6.dto.ProfileRequestDTO;
import com.pigeonchat.lab6.dto.ProfileResponseDTO;
import com.pigeonchat.lab6.entity.Account;
import com.pigeonchat.lab6.entity.Profile;
import com.pigeonchat.lab6.mappers.AccountMapper;
import com.pigeonchat.lab6.mappers.ProfileMapper;
import com.pigeonchat.lab6.repository.AccountRepository;
import com.pigeonchat.lab6.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    private final AccountMapper accountMapper = new AccountMapper();
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private ProfileService profileService;

    private final ProfileMapper profileMapper = new ProfileMapper();

    public AccountResponseDTO createAccount(AccountRequestDTO accountRequestDTO) {
        if (accountRepository.findByLogin(accountRequestDTO.getLogin()).isPresent())
            throw new IllegalArgumentException("Аккаунт с таким логином уже существует");

        String hashedPassword = passwordEncoder.encode(accountRequestDTO.getPassword());

        Account account = new Account();
        account.setLogin(accountRequestDTO.getLogin());
        account.setPassword(hashedPassword);

        ProfileResponseDTO profile = profileService.createProfile(accountRequestDTO.getProfile());
        account.setProfile(profileMapper.toEntity(profile));

        Account savedAccount = accountRepository.save(account);

        return accountMapper.toResponseDTO(savedAccount, AccountResponseDTO.class);
    }

    public AccountResponseDTO getAccountByLogin(String login) {
        Account account = accountRepository.findByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("Аккаунт не найден"));
        return accountMapper.toResponseDTO(account, AccountResponseDTO.class);
    }

    public void updatePassword(String login, String oldPassword, String newPassword) {
        Account account = accountRepository.findByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("Аккаунт не найден"));

        if (!passwordEncoder.matches(oldPassword, account.getPassword())) {
            throw new RuntimeException("Не верный пароль");
        }

        String hashedPassword = passwordEncoder.encode(newPassword);
        account.setPassword(hashedPassword);

        accountRepository.save(account);
    }

    @Transactional
    public void deleteAccount(String login, String password) {
        Account account = accountRepository.findByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("Аккаунт не найден"));

        if (!passwordEncoder.matches(password, account.getPassword())) {
            throw new RuntimeException("Не верный пароль");
        }

        accountRepository.deleteByLogin(login);
    }
}
