package com.volkan.config.rabbitmq;


import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    // auth-microserviste oluşturduğumuz kuyruğu user microserviste de
    // configuration ayarları ile beraber oluşturuyoruz.

    @Value("${rabbitmq.queueregisterelastic}")
    private String elasticRegisterQueue;

    @Bean
    Queue registerQueueElastic() {
        return new Queue(elasticRegisterQueue);
    }
}
