package com.UserRegistraion.service;

import com.UserRegistraion.entities.User;
import com.UserRegistraion.payload.UserDTO;

import java.util.List;

public interface UserService {
    User saveUser(User user);

    List<User> getAllUsers();
}


