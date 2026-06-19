package com.financebridge.onboarding.repository;

import com.financebridge.onboarding.entity.CustomerOutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerOutboxRepository extends JpaRepository<CustomerOutboxEntity,Long> {
}
