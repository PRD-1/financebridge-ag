package com.financebridge.onboarding.service;

import com.financebridge.onboarding.config.TransactionConfig;
import com.financebridge.onboarding.dto.CustomerInterestRequest;
import com.financebridge.onboarding.dto.CustomerInterestResponse;
import com.financebridge.onboarding.dto.CustomerList;
import com.financebridge.onboarding.entity.Customer;
import com.financebridge.onboarding.entity.CustomerOutboxEntity;
import com.financebridge.onboarding.event.CustomerInterestEvent;
import com.financebridge.onboarding.repository.CustomerOutboxRepository;
import com.financebridge.onboarding.repository.CustomerRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
public class CustomerOnboardingService {
    private final CustomerRepository customerRepository;
    private final CustomerOutboxRepository customerOutboxRepository;

    private final KafkaTemplate<String, CustomerInterestEvent> kafkaTemplate;

    public CustomerOnboardingService(CustomerRepository customerRepository, CustomerOutboxRepository customerOutboxRepository, KafkaTemplate<String, CustomerInterestEvent> kafkaTemplate) {
        this.customerRepository = customerRepository;
        this.customerOutboxRepository=customerOutboxRepository;
        this.kafkaTemplate=kafkaTemplate;
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = "chainedKafkaTransactionManager")
    public CustomerInterestResponse customerInterest(CustomerInterestRequest customerInterestRequest){
        if (customerRepository.existsByEmail(customerInterestRequest.getEMail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        Customer customer = new Customer();
        customer.setName(customerInterestRequest.getName());
        customer.setEmail(customerInterestRequest.getEMail());
         customerRepository.save(customer);
        CustomerOutboxEntity customerOutboxEntity = new CustomerOutboxEntity();
        customerOutboxEntity.setCustomerId(String.valueOf(customer.getId()));
        customerOutboxEntity.setStatus("Pending");
        customerOutboxEntity.setEventType("CUSTOMER_INTEREST_SUBMITTED");
        customerOutboxEntity.setEventId(UUID.randomUUID().toString());
         customerOutboxRepository.save(customerOutboxEntity);
        CustomerInterestEvent customerInterestEvent=new CustomerInterestEvent(customerOutboxEntity.getCustomerId(),customer.getName(), customer.getEmail() );
        kafkaTemplate.send("customer-interest-submitted", customerInterestEvent);
        customerOutboxEntity.setStatus("Sent");
        return new CustomerInterestResponse("Thank you for your Interest and we received your Request and soon you will receive an Email from our side", 200);
    }
    public List<CustomerList> getCustomers() {
  return customerRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    private CustomerList mapToResponse(Customer customer) {
        CustomerList customerlist = new CustomerList();
        customerlist.setEmail(customer.getEmail());
        customerlist.setId(customer.getId());
        customerlist.setName(customer.getName());
        return customerlist;
    }
}
