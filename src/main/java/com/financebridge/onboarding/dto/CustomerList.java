package com.financebridge.onboarding.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerList {

    private long Id;
    private String name;
    private String email;
}
