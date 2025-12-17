package com.hyus4ki.asyncpro.service;
import com.hyus4ki.asyncpro.dto.ProcessMessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static com.hyus4ki.asyncpro.config.RabbitMQConfig.*;

@Slf4j
@Service
public class DLQService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendToDLQ(ProcessMessageDTO message, String errorReason) {
        message.setErrorReason(errorReason);
        message.setRetryCount(message.getRetryCount() + 1);
        log.warn("Enviando mensagem {} para DLQ. Tentativa: {}",
                message.getId(), message.getRetryCount());
        rabbitTemplate.convertAndSend(DLX, DLQ, message);
    }
}