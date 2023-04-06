package com.example.tg.api;

import lombok.Data;

@Data
public class RegisterDTO {
//    @JsonIgnore
//    private int stateId;
//    @JsonIgnore
//    private Long chatId;
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    public RegisterDTO(String username, String email, String password, String firstName, String lastName) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public RegisterDTO() {

    }
}
