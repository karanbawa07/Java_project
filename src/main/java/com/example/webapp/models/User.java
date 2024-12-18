package com.example.webapp.models;

import jakarta.persistence.*;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users") // Optional, you can define a custom table name if needed
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private boolean enabled;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Authority> authorities;
}
