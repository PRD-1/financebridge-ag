package com.financebridge.onboarding.consumer;

import com.financebridge.onboarding.event.CustomerInterestEvent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service
public class SendCustomerEmailConsumer {


    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public SendCustomerEmailConsumer(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @KafkaListener(topics="customer-interest-submitted",groupId="email-consumer-group")

    public void sendCustomerEmail(CustomerInterestEvent customerInterestEvent) throws MessagingException {
        Context context = new Context();
        context.setVariable("anrede", customerInterestEvent.getAnrede());
        context.setVariable("vorname", customerInterestEvent.getCustomerVorName());
        context.setVariable("link","http://localhost:4200/personal-information?token=" + customerInterestEvent.getToken());
        String htmlContent = templateEngine.process("CustomerInterestTemplate", context);
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(customerInterestEvent.getCustomerEmail());
        helper.setSubject("FinanceBridge AG welcomes You");
        helper.setText(htmlContent,true);
        helper.addInline("fbLogo", new ClassPathResource("static/images/financebridge-logo.png"));
        javaMailSender.send(message);
    }


    @KafkaListener(topics="customer-information-submitted",groupId="email-consumer-group")
    public void sendCustomerInformationEmail(CustomerInterestEvent customerInterestEvent) throws MessagingException {
        Context context = new Context();
        context.setVariable("anrede", customerInterestEvent.getAnrede());
        context.setVariable("vorname", customerInterestEvent.getCustomerVorName());
        context.setVariable("bevorzugterName", customerInterestEvent.getBevorzugterName());
        context.setVariable("link","http://localhost:4200");
        String htmlContent = templateEngine.process("CustomerInformationTemplate", context);
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(customerInterestEvent.getCustomerEmail());
        helper.setSubject("We received your Information");
        helper.setText(htmlContent,true);
        helper.addInline("fbLogo", new ClassPathResource("static/images/financebridge-logo.png"));
        javaMailSender.send(message);
    }
}
