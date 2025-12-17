package com.hyus4ki.asyncpro.service;
import com.hyus4ki.asyncpro.dto.ProcessMessageDTO;
import com.hyus4ki.asyncpro.exception.ProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Random;

@Slf4j
@Service
public class MessageProcessorService {
    private final Random random = new Random();

    public void processMessage(ProcessMessageDTO message) {
        log.info("Processando mensagem: {}", message.getId());
        try {
            Thread.sleep(500);
            if (random.nextInt(100) < 30) {
                throw new ProcessingException("Erro simulado no processamento", "ERROR_001");
            }
            log.info("Mensagem {} processada com sucesso", message.getId());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ProcessingException("Processamento interrompido", "ERROR_002");
        }
    }
}