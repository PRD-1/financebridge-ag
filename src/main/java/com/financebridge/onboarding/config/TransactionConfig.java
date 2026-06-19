package com.financebridge.onboarding.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.transaction.ChainedKafkaTransactionManager;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;

@Configuration
public class TransactionConfig {

    @Bean
    public JpaTransactionManager jpaTransactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public KafkaTransactionManager kafkaTransactionManager(KafkaTemplate kafkaTemplate) {
        return new KafkaTransactionManager<>(kafkaTemplate.getProducerFactory());
    }


    @Primary
    @Qualifier("transactionManager")
    @Bean
    public ChainedKafkaTransactionManager chainedKafkaTransactionManager(JpaTransactionManager jpaTransactionManager, KafkaTransactionManager kafkaTransactionManager) {
        return new ChainedKafkaTransactionManager(kafkaTransactionManager,jpaTransactionManager);
    }
}