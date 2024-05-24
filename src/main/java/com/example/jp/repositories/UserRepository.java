package com.example.jp.repositories;


import com.example.jp.model.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    public User findUserByEmail(String email){
        return new User(email,"123456");
    }
}