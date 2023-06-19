package com.UserRegistraion.service.impl;

import com.UserRegistraion.entities.User;
import com.UserRegistraion.exception.UserAlreadyExistsException;
import com.UserRegistraion.payload.UserDTO;
import com.UserRegistraion.repositories.UserRepository;
import com.UserRegistraion.service.UserService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}

