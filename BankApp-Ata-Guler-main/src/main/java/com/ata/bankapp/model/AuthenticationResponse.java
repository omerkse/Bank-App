package com.ata.bankapp.model;

import lombok.Getter;
import java.io.Serializable;

@Getter
public class AuthenticationResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String jwt;
    private final Long userId;
    private final String username;

    public AuthenticationResponse(String jwt, Long userId, String username) {
        this.jwt = jwt;
        this.userId = userId;
        this.username = username;
    }

    // Eski constructor'ı da tutabilirsiniz geriye dönük uyumluluk için
    public AuthenticationResponse(String jwt) {
        this(jwt, null, null);
    }
}