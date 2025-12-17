package com.hyus4ki.asyncpro.listener;
import com.hyus4ki.asyncpro.dto.ProcessMessageDTO;
import com.hyus4ki.asyncpro.service.DLQService;
import com.hyus4ki.asyncpro.service.MessageProcessorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import static com.hyus4ki.asyncpro.config.RabbitMQConfig.*;

@Slf4j
@Component
public class MessageListener {
    @Autowired
    private MessageProcessorService processorService;
    @Autowired
    private DLQService dlqService;

    @RabbitListener(queues = MAIN_QUEUE)
    public void processMessage(@Payload ProcessMessageDTO message) {
        try {
            processorService.processMessage(message);
        } catch (Exception e) {
            dlqService.sendToDLQ(message, e.getMessage());
        }
    }

    @RabbitListener(queues = DLQ)
    public void processDLQMessage(@Payload ProcessMessageDTO message) {
        log.error("MENSAGEM NA DLQ - ID: {}, Erro: {}, Tentativas: {}",
                message.getId(), message.getErrorReason(), message.getRetryCount());
    }
}