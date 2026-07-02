package com.financebridge.onboarding.service;

import com.financebridge.onboarding.entity.FailedEventEntity;
import com.financebridge.onboarding.repository.FailedEventRepository;
import org.springframework.stereotype.Service;

@Service
public class FailedEventService {

    private final FailedEventRepository failedEventRepository;


    public FailedEventService(FailedEventRepository failedEventRepository) {
        this.failedEventRepository = failedEventRepository;
    }



    public void save(String payload, String originalTopic, int partition, long offset, String errorMessage) {
        FailedEventEntity failedEventEntity = new FailedEventEntity();
        failedEventEntity.setPayload(payload);
        failedEventEntity.setKafkaOffset(offset);
        failedEventEntity.setPartition(partition);
        failedEventEntity.setTopic(originalTopic);
        failedEventEntity.setErrorMessage(errorMessage);
        failedEventRepository.save(failedEventEntity);
    }
}
