package com.example.todolistcoursework.domain.services;

import com.example.todolistcoursework.domain.entities.User;
import com.example.todolistcoursework.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public User register(User user) {
        return userRepository.save(user);
    }
}
