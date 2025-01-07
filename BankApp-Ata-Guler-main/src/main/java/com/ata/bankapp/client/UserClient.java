package com.ata.bankapp.client;

import com.ata.bankapp.model.User;
import com.ata.bankapp.model.AuthenticationRequest;
import com.ata.bankapp.model.AuthenticationResponse;
import com.ata.bankapp.model.MessageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service", url = "http://localhost:8080/api/users")
public interface UserClient {

    @PostMapping("/internal/register")
    MessageResponse registerUser(@RequestBody User user);

    @PostMapping("/login")
    AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest);

    @GetMapping("/{userId}")
    User getUser(@PathVariable("userId") Long userId);

    @PutMapping("/{userId}")
    User updateUser(@PathVariable("userId") Long userId, @RequestBody User updatedUser);
}
