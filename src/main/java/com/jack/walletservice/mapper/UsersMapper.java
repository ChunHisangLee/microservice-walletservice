package com.jack.walletservice.mapper;

import com.jack.walletservice.dto.UsersDTO;
import com.jack.walletservice.entity.Users;
import org.springframework.stereotype.Component;

@Component
public class UsersMapper {

    public UsersDTO toDto(Users users) {
        return UsersDTO.builder()
                .id(users.getId())
                .name(users.getName())
                .email(users.getEmail())
                .build();
    }

    public Users toEntity(UsersDTO usersDTO) {
        return Users.builder()
                .name(usersDTO.getName())
                .email(usersDTO.getEmail())
                .password(usersDTO.getPassword()) // Ensure password is mapped if provided
                .build();
    }
}
