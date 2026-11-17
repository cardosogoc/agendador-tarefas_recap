package com.javanauta.agendadortarefas.infrastructure.repository;

import com.javanauta.agendadortarefas.infrastructure.entity.MensagemFalha;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MensagemRepository extends MongoRepository<MensagemFalha, String> {
}
