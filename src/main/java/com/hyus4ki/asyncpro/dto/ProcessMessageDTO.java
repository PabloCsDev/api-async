package com.hyus4ki.asyncpro.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessMessageDTO {
    private String id;
    private String content;
    private LocalDateTime timestamp;
    private Integer retryCount;
    private String errorReason;

    public ProcessMessageDTO(String content) {
        this.id = UUID.randomUUID().toString();
        this.content = content;
        this.timestamp = LocalDateTime.now();
        this.retryCount = 0;
    }
}