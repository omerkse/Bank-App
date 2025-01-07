package com.ata.bankapp.service;

import com.ata.bankapp.model.User;
import com.ata.bankapp.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(User user) {
        logger.info("Registering new user: {}", user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        logger.info("User registered successfully with ID: {}", savedUser.getId());
        return savedUser;
    }

    @Override
    public User findByUsername(String username) {
        logger.info("Finding user by username: {}", username);
        User user = userRepository.findByUsername(username);
        if (user == null) {
            logger.warn("User not found with username: {}", username);
        }
        return user;
    }

    @Override
    public User updateUser(Long userId, User updatedUser) {
        logger.info("Updating user with ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User not found with ID: {}", userId);
                    return new RuntimeException("User not found");
                });
        user.setEmail(updatedUser.getEmail());
        user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        User updatedUserResult = userRepository.save(user);
        logger.info("User updated successfully with ID: {}", updatedUserResult.getId());
        return updatedUserResult;
    }

    @Override
    public User getUser(Long userId) {
        logger.info("Fetching user with ID: {}", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User not found with ID: {}", userId);
                    return new RuntimeException("User not found");
                });
    }
}
