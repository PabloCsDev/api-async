package com.hyus4ki.asyncpro.controller;
import com.hyus4ki.asyncpro.dto.ProcessMessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static com.hyus4ki.asyncpro.config.RabbitMQConfig.*;

@Slf4j
@RestController
@RequestMapping("/api/messages")
public class MessageController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping
    public ResponseEntity<String> sendMessage(@RequestBody String content) {
        ProcessMessageDTO message = new ProcessMessageDTO(content);
        rabbitTemplate.convertAndSend(MAIN_EXCHANGE, MAIN_QUEUE, message);
        return ResponseEntity.ok("Mensagem " + message.getId() + " enviada para processamento");
    }
}