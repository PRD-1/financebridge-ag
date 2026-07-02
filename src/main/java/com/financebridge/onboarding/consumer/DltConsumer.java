package com.financebridge.onboarding.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financebridge.onboarding.event.CustomerInterestEvent;
import com.financebridge.onboarding.service.FailedEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class DltConsumer {


    private final FailedEventService failedEventService;

    private static final Logger log = LoggerFactory.getLogger(DltConsumer.class);

    public DltConsumer(FailedEventService failedEventService) {
        this.failedEventService = failedEventService;
    }

    @KafkaListener(topics = {
            "customer-interest-submitted-dlt",
            "customer-information-submitted-dlt"
    } , groupId="dlt-consumer-group", containerFactory = "dltKafkaListenerContainerFactory")
    public void handleDlt(
            @Payload CustomerInterestEvent payload,
            @Header(KafkaHeaders.DLT_ORIGINAL_TOPIC) String originalTopic,
            @Header(KafkaHeaders.DLT_ORIGINAL_PARTITION) int partition,
            @Header(KafkaHeaders.DLT_ORIGINAL_OFFSET) long offset,
            @Header(KafkaHeaders.DLT_EXCEPTION_MESSAGE) String errorMessage) {

        try {
            String payloadJson = new ObjectMapper().writeValueAsString(payload);
            failedEventService.save(payloadJson, originalTopic, partition, offset, errorMessage);
        } catch (Exception e) {
            log.error("DLT consumer failed: {}", e.getMessage(), e);
        }

    }
}
