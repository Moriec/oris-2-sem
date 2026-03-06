package com.vinogradov.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private Integer id;
    private String name;
    private String lastname;
    private String login;

    public UserDTO(Integer id, String name, String lastname, String login) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.login = login;
    }
}
