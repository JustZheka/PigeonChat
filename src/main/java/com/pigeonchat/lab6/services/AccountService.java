package com.pigeonchat.lab6.services;

import com.pigeonchat.lab6.dto.AccountRequestDTO;
import com.pigeonchat.lab6.dto.AccountResponseDTO;
import com.pigeonchat.lab6.entity.Account;
import com.pigeonchat.lab6.exceptions.AccountNotFoundException;
import com.pigeonchat.lab6.exceptions.IncorrectPasswordException;
import com.pigeonchat.lab6.exceptions.LoginAlreadyExistException;
import com.pigeonchat.lab6.mappers.AccountMapper;
import com.pigeonchat.lab6.mappers.ProfileMapper;
import com.pigeonchat.lab6.repository.AccountRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class AccountService {
    AccountRepository accountRepository;
    ProfileService profileService;
    AccountMapper accountMapper;
    ProfileMapper profileMapper;
    PasswordEncoder passwordEncoder;

    public AccountResponseDTO createAccount(@NonNull final AccountRequestDTO accountRequestDTO) {
        accountRepository.findByLogin(accountRequestDTO.getLogin())
            .ifPresent(_ -> {
                throw new LoginAlreadyExistException("Аккаунт с таким логином уже существует");
            });

        val account = Account.builder()
            .login(accountRequestDTO.getLogin())
            .password(passwordEncoder.encode(accountRequestDTO.getPassword()))
            .build();

        val profile = profileService.createProfile(accountRequestDTO.getProfile());
        account.setProfile(profileMapper.toEntity(profile));

        return accountMapper.toResponseDTO(accountRepository.save(account));
    }

    public AccountResponseDTO getAccountByLogin(@NonNull final String login) {
        return accountRepository.findByLogin(login)
            .map(accountMapper::toResponseDTO)
            .orElseThrow(() -> new AccountNotFoundException("Аккаунт не найден"));
    }

    public void updatePassword(@NonNull final String login, @NonNull final String oldPassword, @NonNull final String newPassword) {
        val account = accountRepository.findByLogin(login)
            .orElseThrow(() -> new AccountNotFoundException("Аккаунт не найден"));

        if (!passwordEncoder.matches(oldPassword, account.getPassword())) {
            throw new IncorrectPasswordException("Неверный пароль");
        }

        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
    }

    public void deleteAccount(@NonNull final String login, @NonNull final String password) {
        val account = accountRepository.findByLogin(login)
            .orElseThrow(() -> new AccountNotFoundException("Аккаунт не найден"));

        if (!passwordEncoder.matches(password, account.getPassword())) {
            throw new IncorrectPasswordException("Неверный пароль");
        }

        accountRepository.delete(account);
    }
}