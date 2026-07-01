package com.financebridge.onboarding.event;

import com.financebridge.onboarding.entity.Anrede;
import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CustomerInterestEvent {

    private String customerId;

    private String anrede;

    private String customerVorName;

    private String customerNachName;

    private String bevorzugterName;

    private String  customerEmail;

    private String token;
}
