package com.volkan.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mail atmak için bir kuyruk oluşturup exchangimize binding
 * yapacağız daha sonra producer üzerinde bu kuyruğa bir model göndereceğiz
 */
@Configuration
public class RabbitMqConfig {
    @Value("${rabbitmq.exchange-auth}")
    private String exchange;
    @Value("${rabbitmq.registerkey}")
    private String registerBindingKey;
    @Value("${rabbitmq.queueRegister}")
    private String queueNameRegister;
    @Value("${rabbitmq.registermailkey}")
    private String registerMailBindingKey;
    @Value("${rabbitmq.registermailqueue}")
    private String registerMailQueue;

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(exchange);
    }
    @Bean
    Queue registerQueue() {
        return new Queue(queueNameRegister);
    }
    @Bean
    public Binding bindingRegister(final Queue registerQueue, final DirectExchange exchangeAuth) {
        return BindingBuilder.bind(registerQueue).to(exchangeAuth).with(registerBindingKey);
    }
    @Bean
    Queue registerMailQueue() {
        return new Queue(registerMailQueue);
    }
    @Bean
    public Binding bindingRegisterMail(final Queue registerMailQueue, final DirectExchange exchangeAuth) {
        return BindingBuilder.bind(registerMailQueue).to(exchangeAuth).with(registerMailBindingKey);
    }

}
