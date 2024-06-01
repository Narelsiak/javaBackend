package com.example.jp.services;

import com.example.jp.model.User;
import com.example.jp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public boolean checkIfValidOldPassword(User user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    public void changeUserPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void registerUser(User user) throws Exception {
        if (userRepository.findUserByEmail(user.getEmail()) != null) {
            throw new Exception("User with this email already exists");
        }
        if(userRepository.findUserByNickname(user.getUsername()) != null) {
            throw new Exception("User with this nickname already exists");
        }
        user.setRole("USER");
        userRepository.save(user);
    }
}
