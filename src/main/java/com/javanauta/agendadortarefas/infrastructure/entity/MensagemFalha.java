package com.javanauta.agendadortarefas.infrastructure.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("mensageria-falha")
public class MensagemFalha {

    @Id
    private String id;
    private String topico;
    private String mensagem;
    private String stackTrace;
    private LocalDateTime dataFalha;
}
