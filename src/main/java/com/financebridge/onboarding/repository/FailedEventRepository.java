package com.financebridge.onboarding.repository;

import com.financebridge.onboarding.entity.FailedEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FailedEventRepository extends JpaRepository<FailedEventEntity, UUID> {
}
