package com.financebridge.onboarding.controller;

import com.financebridge.onboarding.dto.CustomerInterestRequest;
import com.financebridge.onboarding.dto.CustomerInterestResponse;
import com.financebridge.onboarding.dto.CustomerList;
import com.financebridge.onboarding.dto.CustomerPersonalInformationRequest;
import com.financebridge.onboarding.service.CustomerOnboardingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomerOnboardingController {


    private final CustomerOnboardingService customerOnboardingService;

    public CustomerOnboardingController(CustomerOnboardingService customerOnboardingService) {
        this.customerOnboardingService = customerOnboardingService;
    }

    @PostMapping("/financebridge/interest")
    public ResponseEntity<CustomerInterestResponse> customerInterest (@Valid @RequestBody CustomerInterestRequest customerInterestRequest){
        CustomerInterestResponse customerInterestResponse = customerOnboardingService.customerInterest(customerInterestRequest);
        return ResponseEntity.ok(customerInterestResponse);
    }

    @PostMapping("/financebridge/personalInformation/{customerId}")
    public ResponseEntity<CustomerInterestResponse> customerPersonalInformation (@Valid @RequestBody CustomerPersonalInformationRequest customerPersonalInformationRequest , @PathVariable Long customerId){
        CustomerInterestResponse customerInterestResponse = customerOnboardingService.savingCustomerPersonalInformation(customerPersonalInformationRequest,customerId);
        return ResponseEntity.ok(customerInterestResponse);
    }

    @GetMapping("/customers")
    public List<CustomerList> getCustomers() {
        return customerOnboardingService.getCustomers();
    }
}
