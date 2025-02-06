package com.pigeonchat.lab6.services;

import com.pigeonchat.lab6.dto.AccountRequestDTO;
import com.pigeonchat.lab6.dto.AccountResponseDTO;
import com.pigeonchat.lab6.dto.ProfileResponseDTO;
import com.pigeonchat.lab6.entity.Account;
import com.pigeonchat.lab6.mappers.AccountMapper;
import com.pigeonchat.lab6.mappers.ProfileMapper;
import com.pigeonchat.lab6.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class AccountService {
    private AccountRepository accountRepository;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private ProfileService profileService;
    private AccountMapper accountMapper;
    private ProfileMapper profileMapper;

    public AccountResponseDTO createAccount(final AccountRequestDTO accountRequestDTO) {
        if (accountRepository.findByLogin(accountRequestDTO.getLogin()).isPresent()) {
            throw new IllegalArgumentException("Аккаунт с таким логином уже существует");
        }

        String hashedPassword = passwordEncoder.encode(accountRequestDTO.getPassword());

        Account account = new Account();
        account.setLogin(accountRequestDTO.getLogin());
        account.setPassword(hashedPassword);

        ProfileResponseDTO profile = profileService.createProfile(accountRequestDTO.getProfile());
        account.setProfile(profileMapper.toEntity(profile));

        Account savedAccount = accountRepository.save(account);

        return accountMapper.toResponseDTO(savedAccount);
    }

    public AccountResponseDTO getAccountByLogin(final String login) {
        Account account = accountRepository.findByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("Аккаунт не найден"));
        return accountMapper.toResponseDTO(account);
    }

    public void updatePassword(final String login, final String oldPassword, final String newPassword) {
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
    public void deleteAccount(final String login, final String password) {
        Account account = accountRepository.findByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("Аккаунт не найден"));

        if (!passwordEncoder.matches(password, account.getPassword())) {
            throw new RuntimeException("Не верный пароль");
        }

        accountRepository.deleteByLogin(login);
    }
}
