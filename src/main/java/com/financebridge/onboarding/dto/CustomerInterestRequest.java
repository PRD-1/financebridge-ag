package com.financebridge.onboarding.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.bind.annotation.RequestParam;

@Getter
@Setter
@ToString
public class CustomerInterestRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email")
    String email;

    @NotBlank(message = "anrede is required")
    private String anrede;


    @NotBlank(message = "vorname is required")
    String vorname;

    @NotBlank(message = "nachname is required")
    String nachname;

    @NotBlank(message="customerType is required")
    String customerType;
}
