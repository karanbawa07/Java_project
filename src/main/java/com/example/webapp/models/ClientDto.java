package com.example.webapp.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto {
    @NotEmpty(message = "The first name is required")
    private String firstName;

    @NotEmpty(message = "The last name is required")
    private String lastName;

    @NotEmpty(message = "The email name is required")
    @Email
    private String email;

    private String phone;
    private String address;

    @NotEmpty(message = "The Status is required")
    private String status;
}
