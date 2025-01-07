package com.ata.bankapp.request;

import lombok.Data;

@Data
public class UserLoginRequest {
    private String username;
    private String password;
    private Long id;
}
