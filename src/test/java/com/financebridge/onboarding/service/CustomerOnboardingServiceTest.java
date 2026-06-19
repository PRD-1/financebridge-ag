package com.financebridge.onboarding.service;

import com.financebridge.onboarding.dto.CustomerInterestRequest;
import com.financebridge.onboarding.repository.CustomerOutboxRepository;
import com.financebridge.onboarding.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CustomerOnboardingServiceTest {

    @Autowired
    private CustomerOnboardingService customerOnboardingService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerOutboxRepository customerOutboxRepository;

    @Test
    void customerInterest_savesCustomerAndOutboxWithSentStatus() {
        CustomerInterestRequest request = new CustomerInterestRequest();
        request.setName("Test User");
        request.setEMail("testuser" + System.currentTimeMillis() + "@example.com");

        customerOnboardingService.customerInterest(request);

        boolean customerExists = customerRepository.existsByEmail(request.getEMail());
        assertThat(customerExists).isTrue();

        var outboxRecords = customerOutboxRepository.findAll();
        assertThat(outboxRecords).isNotEmpty();
    }
}