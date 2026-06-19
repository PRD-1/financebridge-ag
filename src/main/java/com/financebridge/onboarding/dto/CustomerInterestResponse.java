package com.financebridge.onboarding.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Setter
@Getter
@ToString
public class CustomerInterestResponse {
    
    private String message;
    private int responseCode;

    public CustomerInterestResponse(String s, int i) {
        this.message=s;
        this.responseCode=i;
    }
}
