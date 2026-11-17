package com.javanauta.agendadortarefas.infrastructure.message.kafka.consumer;

import com.javanauta.agendadortarefas.business.TarefasService;
import com.javanauta.agendadortarefas.business.dto.StatusTarefaDTO;
import com.javanauta.agendadortarefas.infrastructure.entity.MensagemFalha;
import com.javanauta.agendadortarefas.infrastructure.repository.MensagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ConsumerKafka {

    private final ObjectMapper objectMapper;
    private final TarefasService service;
    private final MensagemRepository repository;

    @RetryableTopic(
            attempts = "3",
            backOff = @BackOff(delay = 3000, multiplier = 2.0),
            autoCreateTopics = "true",
            dltStrategy = DltStrategy.FAIL_ON_ERROR
    )
    @KafkaListener(topics = "status-notificacao",
            groupId = "grupo-agendador",
            containerFactory = "kafkaListenerContainerFactory")
    public void consumirStatusTarefa(String mensagem){
        StatusTarefaDTO dto = objectMapper.readValue(mensagem,StatusTarefaDTO.class);
        service.alterarStatus(dto.getStatusNotificacaoEnum(), dto.getId());
    }

    @DltHandler
    public void recuperarMensagemFalha(String mensagem,
                                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                       Throwable exception){

        MensagemFalha mensagemFalha = MensagemFalha.builder()
                .topico(topic)
                .dataFalha(LocalDateTime.now())
                .stackTrace(exception.getMessage())
                .mensagem(mensagem)
                .build();

        repository.save(mensagemFalha);
    }
}
