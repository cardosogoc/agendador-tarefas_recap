package com.javanauta.agendadortarefas.business;

import com.javanauta.agendadortarefas.business.dto.TarefasRecord;
import com.javanauta.agendadortarefas.business.mapper.TarefasMapper;
import com.javanauta.agendadortarefas.business.mapper.TarefasMapperUpdate;
import com.javanauta.agendadortarefas.infrastructure.entity.TarefasEntity;
import com.javanauta.agendadortarefas.infrastructure.enums.StatusNotificacaoEnum;
import com.javanauta.agendadortarefas.infrastructure.exceptions.ResourceNotFoundException;
import com.javanauta.agendadortarefas.infrastructure.repository.TarefasRepository;
import com.javanauta.agendadortarefas.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TarefasService {

    private final TarefasRepository repository;
    private final TarefasMapper mapper;
    private final TarefasMapperUpdate mapperUpdate;
    private final JwtUtil jwtUtil;

    public TarefasRecord gravarTarefa(String token, TarefasRecord dto) {
        String email = jwtUtil.extrairEmailToken(token.substring(7));

        TarefasRecord dtoFinal = new TarefasRecord(
                null, dto.nomeTarefa(), dto.descricao(), LocalDateTime.now(),
                dto.dataEvento(), email,null, StatusNotificacaoEnum.PENDENTE);

        TarefasEntity entity = mapper.paraTarefaEntity(dtoFinal);
        return mapper.paraTarefaDTO(repository.save(entity));
    }

    public List<TarefasRecord> buscaTarefasAgendadasPorPeriodo
            (LocalDateTime dataInicial, LocalDateTime dataFinal) {

        return mapper.paraListaTarefasRecord(repository.findByDataEventoBetweenAndStatusNotificacaoEnum(dataInicial, dataFinal, StatusNotificacaoEnum.PENDENTE));
    }

    public List<TarefasRecord> buscarTarefasPorEmailUsuario(String token) {
        String emailUsuario = jwtUtil.extrairEmailToken(token.substring(7));

        return mapper.paraListaTarefasRecord(repository.findByEmailUsuario(emailUsuario));
    }

    public void deletaTarefaPorID(String id) {
        TarefasEntity entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Tarefa não encontrada, ID inexistente: " + id));

        repository.delete(entity);
    }

    public TarefasRecord alterarStatus(StatusNotificacaoEnum status, String id) {
        TarefasEntity entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Tarefa não encontrada" + id));

        entity.setStatusNotificacaoEnum(status);
        TarefasEntity tarefaStatusAtualizado = repository.save(entity);
        return mapper.paraTarefaDTO(tarefaStatusAtualizado);
    }

    public TarefasRecord updateTarefa(TarefasRecord dto, String id) {
        TarefasEntity entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Tarefa não encontrada" + id));

        mapperUpdate.updateTarefas(dto, entity);

        TarefasEntity tarefaAtualizada = repository.save(entity);
        return mapper.paraTarefaDTO(tarefaAtualizada);
    }


}
