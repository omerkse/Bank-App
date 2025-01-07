package com.ata.bankapp.service;

import com.ata.bankapp.model.User;

public interface UserService {
    User registerUser(User user);
    User findByUsername(String username);
    User updateUser(Long userId, User updatedUser);
    User getUser(Long userId);
}
