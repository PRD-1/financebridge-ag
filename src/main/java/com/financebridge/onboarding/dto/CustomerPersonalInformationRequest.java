package com.financebridge.onboarding.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class CustomerPersonalInformationRequest {

    private String bevorzugterName;

    @NotBlank(message = "nationalitaet is required")
    private String nationalitaet;

    @NotNull(message = "geburtsDatum is required")
    private LocalDate geburtsdatum;

    @NotBlank(message = "telefonnummer is required")
    private String telefonnummer;

    @NotBlank(message = "ausweisTyp is required")
    private String ausweisTyp;

    @NotBlank(message = "ausweisNummer is required")
    private String ausweisNummer;
}

