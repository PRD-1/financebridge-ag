package com.financebridge.onboarding.config;

import jakarta.persistence.EntityManagerFactory;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.transaction.ChainedKafkaTransactionManager;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class TransactionConfig {

    @Bean
    public JpaTransactionManager jpaTransactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    @Primary
    public ProducerFactory<String, Object> transactionalProducerFactory() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        DefaultKafkaProducerFactory<String, Object> factory = new DefaultKafkaProducerFactory<>(configs);
        factory.setTransactionIdPrefix("fb");
        return factory;
    }

    @Bean
    public ProducerFactory<String, Object> nonTransactionalProducerFactory() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configs);
    }

    @Bean
    @Primary
    public KafkaTemplate<String, Object> transactionalKafkaTemplate(
            @Qualifier("transactionalProducerFactory") ProducerFactory<String, Object> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public KafkaTemplate<String, Object> nonTransactionalKafkaTemplate(
            @Qualifier("nonTransactionalProducerFactory") ProducerFactory<String, Object> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public KafkaTransactionManager kafkaTransactionManager(
            @Qualifier("transactionalProducerFactory") ProducerFactory<String, Object> producerFactory) {
        return new KafkaTransactionManager<>(producerFactory);
    }

    @Primary
    @Qualifier("transactionManager")
    @Bean
    public ChainedKafkaTransactionManager chainedKafkaTransactionManager(
            JpaTransactionManager jpaTransactionManager,
            KafkaTransactionManager kafkaTransactionManager) {
        return new ChainedKafkaTransactionManager(kafkaTransactionManager, jpaTransactionManager);
    }

    @Bean
    public DefaultErrorHandler errorHandler(
            @Qualifier("nonTransactionalKafkaTemplate") KafkaTemplate<String, Object> nonTransactionalKafkaTemplate) {
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(nonTransactionalKafkaTemplate);
        return new DefaultErrorHandler(recoverer, new FixedBackOff(2000L, 2L));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
            ConsumerFactory<String, Object> consumerFactory,
            DefaultErrorHandler defaultErrorHandler,
            ChainedKafkaTransactionManager chainedKafkaTransactionManager) {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(defaultErrorHandler);
        factory.getContainerProperties().setKafkaAwareTransactionManager(chainedKafkaTransactionManager);
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> dltKafkaListenerContainerFactory(
            ConsumerFactory<String, Object> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}