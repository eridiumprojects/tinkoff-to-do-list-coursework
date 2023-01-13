package com.example.todolistcoursework.domain.services;

import com.example.todolistcoursework.domain.entities.User;
import com.example.todolistcoursework.domain.repositories.UserRepository;
import com.example.todolistcoursework.exceptions.ObjectAlreadyExists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public User register(User user) {
        isUserEmailExists(user.getEmail());
        isUserLoginExists(user.getLogin());
        return userRepository.save(user);
    }

    public void isUserEmailExists(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            throw new ObjectAlreadyExists("User with that email is already exists!");
        }
    }

    public void isUserLoginExists(String login) {
        Optional<User> userOptional = userRepository.findByLogin(login);
        if (userOptional.isPresent()) {
            throw new ObjectAlreadyExists("User with that login is already exists!");
        }
    }
}
