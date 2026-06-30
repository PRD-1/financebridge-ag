package com.financebridge.onboarding.service;

import com.financebridge.onboarding.dto.CustomerInterestRequest;
import com.financebridge.onboarding.dto.CustomerInterestResponse;
import com.financebridge.onboarding.dto.CustomerList;
import com.financebridge.onboarding.dto.CustomerPersonalInformationRequest;
import com.financebridge.onboarding.entity.*;
import com.financebridge.onboarding.event.CustomerInterestEvent;
import com.financebridge.onboarding.repository.CustomerOutboxRepository;
import com.financebridge.onboarding.repository.CustomerPersonalInformationRepository;
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

    private final CustomerPersonalInformationRepository customerPersonalInformationRepository;

    private final KafkaTemplate<String, CustomerInterestEvent> kafkaTemplate;

    public CustomerOnboardingService(CustomerRepository customerRepository, CustomerOutboxRepository customerOutboxRepository, CustomerPersonalInformationRepository customerPersonalInformationRepository, KafkaTemplate<String, CustomerInterestEvent> kafkaTemplate) {
        this.customerRepository = customerRepository;
        this.customerOutboxRepository=customerOutboxRepository;
        this.customerPersonalInformationRepository = customerPersonalInformationRepository;
        this.kafkaTemplate=kafkaTemplate;
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = "chainedKafkaTransactionManager")
    public CustomerInterestResponse customerInterest(CustomerInterestRequest customerInterestRequest){
        if (customerRepository.existsByEmail(customerInterestRequest.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        Customer customer = new Customer();
        customer.setAnrede(Anrede.valueOf(customerInterestRequest.getAnrede()));
        customer.setVorname(customerInterestRequest.getVorname());
        customer.setNachname(customerInterestRequest.getNachname());
        customer.setEmail(customerInterestRequest.getEmail());
        customer.setCustomerType(CustomerType.valueOf(customerInterestRequest.getCustomerType()));
        customerRepository.save(customer);
        CustomerOutboxEntity customerOutboxEntity = new CustomerOutboxEntity();
        customerOutboxEntity.setCustomerId(String.valueOf(customer.getId()));
        customerOutboxEntity.setStatus("Pending");
        customerOutboxEntity.setEventType("CUSTOMER_INTEREST_SUBMITTED");
        customerOutboxEntity.setEventId(UUID.randomUUID().toString());
        customerOutboxRepository.save(customerOutboxEntity);
        CustomerInterestEvent customerInterestEvent=new CustomerInterestEvent(customerOutboxEntity.getCustomerId(), customer.getAnrede().toString(),customer.getVorname(), customer.getNachname(), null, customer.getEmail() );
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
        customerlist.setVornname(customer.getVorname());
        customerlist.setNachname(customer.getNachname());
        return customerlist;
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = "chainedKafkaTransactionManager")
    public CustomerInterestResponse savingCustomerPersonalInformation(CustomerPersonalInformationRequest customerPersonalInformationRequest, Long customerId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        CustomerPersonalInformation customerPersonalInformation = new CustomerPersonalInformation();
        customerPersonalInformation.setCustomer(customer);
        customerPersonalInformation.setAusweisNummer(customerPersonalInformationRequest.getAusweisNummer());
        customerPersonalInformation.setNationalitaet(customerPersonalInformationRequest.getNationalitaet());
        customerPersonalInformation.setAusweisTyp(customerPersonalInformationRequest.getAusweisTyp());
        customerPersonalInformation.setBevorzugterName(customerPersonalInformationRequest.getBevorzugterName());
        customerPersonalInformation.setGeburtsDatum(customerPersonalInformationRequest.getGeburtsdatum());
        customerPersonalInformation.setTelefonnummer(customerPersonalInformationRequest.getTelefonnummer());
        customerPersonalInformationRepository.save(customerPersonalInformation);
        CustomerOutboxEntity customerOutboxEntity = new CustomerOutboxEntity();
        customerOutboxEntity.setCustomerId(String.valueOf(customer.getId()));
        customerOutboxEntity.setStatus("Pending");
        customerOutboxEntity.setEventType("CUSTOMER_INFORMATION_SUBMITTED");
        customerOutboxEntity.setEventId(UUID.randomUUID().toString());
        customerOutboxRepository.save(customerOutboxEntity);
        CustomerInterestEvent customerInterestEvent=new CustomerInterestEvent(customerOutboxEntity.getCustomerId(), customer.getAnrede().toString(),customer.getVorname(), customer.getNachname(), customerPersonalInformation.getBevorzugterName(), customer.getEmail() );
        kafkaTemplate.send("customer-information-submitted", customerInterestEvent);
        customerOutboxEntity.setStatus("Sent");
        return new CustomerInterestResponse(" Your Personal Information has been saved and you'll receive soon an email with further steps", 200);
    }
}
