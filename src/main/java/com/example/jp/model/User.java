package com.example.jp.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private long id;

    private String email;

    private String password;

    private String role;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}