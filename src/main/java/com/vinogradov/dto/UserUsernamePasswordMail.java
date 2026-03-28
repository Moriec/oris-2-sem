package com.vinogradov.dto;

public record UserUsernamePasswordMail(
        String username,
        String password,
        String mail
) {
}
