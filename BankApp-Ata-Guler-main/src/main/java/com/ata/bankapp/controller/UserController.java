package com.ata.bankapp.controller;

import com.ata.bankapp.client.UserClient;
import com.ata.bankapp.model.AuthenticationResponse;
import com.ata.bankapp.model.MessageResponse;
import com.ata.bankapp.model.User;
import com.ata.bankapp.request.UserLoginRequest;
import com.ata.bankapp.request.UserRegistrationRequest;
import com.ata.bankapp.security.JwtUtil;
import com.ata.bankapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserClient userClient;

    @Autowired
    private UserService userService;




    @PostMapping("/register")
    public ResponseEntity<MessageResponse> registerUser(@RequestBody UserRegistrationRequest registrationRequest) {
        try {
            logger.info("Registering user with username: {}", registrationRequest.getUsername());
            User user = new User();
            user.setUsername(registrationRequest.getUsername());
            user.setPassword(registrationRequest.getPassword());
            user.setEmail(registrationRequest.getEmail());
            User registeredUser = userService.registerUser(user);


            MessageResponse response = new MessageResponse("User registered successfully with ID: " + registeredUser.getId());
            logger.info("User registered successfully with username: {}", registrationRequest.getUsername());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error while registering user: ", e);
            return ResponseEntity.status(500)
                    .body(new MessageResponse("Registration failed: " + e.getMessage()));
        }
    }

    // UserController.java
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody UserLoginRequest loginRequest) throws Exception {
        logger.info("Attempting to authenticate user with username: {}", loginRequest.getUsername());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            logger.error("Authentication failed for username: {}", loginRequest.getUsername(), e);
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);


        User user = userService.findByUsername(loginRequest.getUsername());

        logger.info("Authentication successful for username: {}", loginRequest.getUsername());
        return ResponseEntity.ok(new AuthenticationResponse(jwt, user.getId(), user.getUsername()));
    }




    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable Long userId) {
        logger.info("Fetching user with ID: {}", userId);
        User user = userService.getUser(userId);
        logger.info("Fetched user with ID: {}", userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody User updatedUser) {
        logger.info("Updating user with ID: {}", userId);
        User updated = userService.updateUser(userId, updatedUser);

        userClient.updateUser(userId, updated);

        logger.info("User with ID: {} updated successfully", userId);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logoutUser(@RequestHeader("Authorization") String token) {
        try {
            String jwtToken = token.substring(7); // "Bearer " kısmını çıkar
            jwtUtil.blacklistToken(jwtToken); // Token'ı kara listeye ekle
            logger.info("Token blacklisted successfully.");
            return ResponseEntity.ok(new MessageResponse("Logout successful."));
        } catch (Exception e) {
            logger.error("Logout failed: ", e);
            return ResponseEntity.status(500).body(new MessageResponse("Logout failed: " + e.getMessage()));
        }
    }


}
