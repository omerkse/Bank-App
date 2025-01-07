package com.ata.bankapp.request;

import lombok.Data;

// Login request için DTO
@Data
public
class LoginRequest {
    private String username;
    private String password;
}
