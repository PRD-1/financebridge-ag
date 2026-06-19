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
    String eMail;

    @NotBlank(message = "name is required")
    String name;

}
