package com.financebridge.onboarding.consumer;

import com.financebridge.onboarding.event.CustomerInterestEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendCustomerEmailConsumer {


    private final JavaMailSender javaMailSender ;

    public SendCustomerEmailConsumer(JavaMailSender javaMailSender){
        this.javaMailSender=javaMailSender;
    }

    @KafkaListener(topics="customer-interest-submitted",groupId="email-consumer-group")

    public void sendCustomerEmail(CustomerInterestEvent customerInterestEvent){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(customerInterestEvent.getCustomerEmail());
        simpleMailMessage.setSubject("FinanceBridgeAG Welcomes You");
        simpleMailMessage.setText("Please Fill up the form with the details");
        javaMailSender.send(simpleMailMessage);
    }
}
