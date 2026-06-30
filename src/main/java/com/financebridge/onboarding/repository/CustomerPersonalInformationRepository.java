package com.financebridge.onboarding.repository;

import com.financebridge.onboarding.entity.CustomerPersonalInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerPersonalInformationRepository extends JpaRepository<CustomerPersonalInformation,Long> {
        }
