package com.skateshop.commons;

import com.skateshop.domain.user.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class UserUtils {

    public static final LocalDateTime CREATED_AT_FIXO =
            LocalDateTime.of(2026, 7, 19, 12, 30);

    public List<User> getUserList() {
        var user1 = User.builder()
                .id(UUID.fromString("626bfad6-d4e3-4a1c-9065-9104fb5dc9fd"))
                .firstName("matheus")
                .lastName("dias")
                .email("matheus@gmail.com")
                .cpf("9999999999")
                .phone("(61)99999-9999")
                .createdAt(CREATED_AT_FIXO)
                .build();

        var user2 = User.builder()
                .id(UUID.fromString("61448ae8-8761-4f4e-ac80-8865db92fcda"))
                .firstName("maria")
                .lastName("luiza")
                .email("luiza@gmail.com")
                .cpf("8888888888")
                .phone("(61)88888-8888")
                .createdAt(CREATED_AT_FIXO)
                .build();

        return List.of(user1, user2);
    }

    public User newUser() {
        return User.builder()
                .id(UUID.fromString("6e18cfa6-eb62-49ce-b4e7-d712286fa160"))
                .firstName("joao")
                .lastName("lucas")
                .email("joao@gmail.com")
                .cpf("77777777777")
                .phone("(61)7777-77777")
                .createdAt(CREATED_AT_FIXO)
                .build();
    }
}
