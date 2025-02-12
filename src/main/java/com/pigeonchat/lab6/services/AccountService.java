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
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class AccountService {
    AccountRepository accountRepository;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    ProfileService profileService;
    AccountMapper accountMapper;
    ProfileMapper profileMapper;

    public AccountResponseDTO createAccount(final AccountRequestDTO accountRequestDTO) {
        if (accountRepository.findByLogin(accountRequestDTO.getLogin()).isPresent()) {
            throw new LoginAlreadyExistException("Аккаунт с таким логином уже существует");
        }

        val hashedPassword = passwordEncoder.encode(accountRequestDTO.getPassword());

        val account = Account.builder()
            .login(accountRequestDTO.getLogin())
            .password(hashedPassword)
            .build();

        val profile = profileService.createProfile(accountRequestDTO.getProfile());
        account.setProfile(profileMapper.toEntity(profile));

        val savedAccount = accountRepository.save(account);

        return accountMapper.toResponseDTO(savedAccount);
    }

    public AccountResponseDTO getAccountByLogin(final String login) {
        val account = accountRepository.findByLogin(login)
                .orElseThrow(() -> new AccountNotFoundException("Аккаунт не найден"));
        return accountMapper.toResponseDTO(account);
    }

    public void updatePassword(final String login, final String oldPassword, final String newPassword) {
        val account = accountRepository.findByLogin(login)
                .orElseThrow(() -> new AccountNotFoundException("Аккаунт не найден"));

        if (!passwordEncoder.matches(oldPassword, account.getPassword())) {
            throw new IncorrectPasswordException("Не верный пароль");
        }

        val hashedPassword = passwordEncoder.encode(newPassword);
        account.setPassword(hashedPassword);

        accountRepository.save(account);
    }

    public void deleteAccount(final String login, final String password) {
        val account = accountRepository.findByLogin(login)
                .orElseThrow(() -> new AccountNotFoundException("Аккаунт не найден"));

        if (!passwordEncoder.matches(password, account.getPassword())) {
            throw new IncorrectPasswordException("Не верный пароль");
        }

        accountRepository.deleteByLogin(login);
    }
}
