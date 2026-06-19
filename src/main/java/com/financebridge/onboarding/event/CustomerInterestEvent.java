package com.financebridge.onboarding.event;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CustomerInterestEvent {

    private String customerId;

    private String customerName;

    private String  customerEmail;

}
