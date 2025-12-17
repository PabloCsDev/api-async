package com.hyus4ki.asyncpro.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class RabbitMQConfig {
    
    public static final String MAIN_QUEUE = "process.queue";
    public static final String DLQ = "process.queue.dlq";
    public static final String MAIN_EXCHANGE = "process.exchange";
    public static final String DLX = "process.dlx";
    
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());  // CORREÇÃO AQUI!
        return new Jackson2JsonMessageConverter(objectMapper);
    }
    
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
    
    @Bean
    public DirectExchange mainExchange() {
        return new DirectExchange(MAIN_EXCHANGE);
    }
    
    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DLX);
    }
    
    @Bean
    public Queue mainQueue() {
        return QueueBuilder.durable(MAIN_QUEUE)
                .withArgument("x-dead-letter-exchange", DLX)
                .withArgument("x-dead-letter-routing-key", DLQ)
                .build();
    }
    
    @Bean
    public Queue dlq() {
        return QueueBuilder.durable(DLQ).build();
    }
    
    @Bean
    public Binding mainBinding() {
        return BindingBuilder.bind(mainQueue())
                .to(mainExchange())
                .with(MAIN_QUEUE);
    }
    
    @Bean
    public Binding dlqBinding() {
        return BindingBuilder.bind(dlq())
                .to(deadLetterExchange())
                .with(DLQ);
    }
}
