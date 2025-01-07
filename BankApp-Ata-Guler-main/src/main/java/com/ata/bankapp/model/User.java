package com.ata.bankapp.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true )
    private String username;

    @Column(nullable = true)
    private String password;

    @Column(nullable = true)
    private String email;
}
