package com.pigeonchat.lab6.mappers;

import com.pigeonchat.lab6.dto.AccountRequestDTO;
import com.pigeonchat.lab6.dto.AccountResponseDTO;
import com.pigeonchat.lab6.entity.Account;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ProfileMapper.class)
public interface AccountMapper {
    @Mapping(source = "profile", target = "profile")
    AccountResponseDTO toResponseDTO(Account account);

    @Mapping(source = "profile", target = "profile")
    Account toEntity(AccountRequestDTO accountRequestDTO);
}
