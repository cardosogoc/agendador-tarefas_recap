package com.javanauta.agendadortarefas.business;

import com.javanauta.agendadortarefas.business.dto.TarefasDTO;
import com.javanauta.agendadortarefas.business.mapper.TarefasMapper;
import com.javanauta.agendadortarefas.infrastructure.entity.TarefasEntity;
import com.javanauta.agendadortarefas.infrastructure.enums.StatusNotificacaoEnum;
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
    private final JwtUtil jwtUtil;

    public TarefasDTO gravarTarefa(String token, TarefasDTO dto){
        String email = jwtUtil.extrairEmailToken(token.substring(7));

        dto.setEmailUsuario(email);
        dto.setDataCriacao(LocalDateTime.now());
        dto.setStatusNotificacaoEnum(StatusNotificacaoEnum.PENDENTE);

        TarefasEntity entity = mapper.paraTarefaEntity(dto);
        return mapper.paraTarefaDTO(repository.save(entity));
    }

    public List<TarefasDTO> buscaTarefasAgendadasPorPeriodo
            (LocalDateTime dataInicial, LocalDateTime dataFinal){

        return mapper.paraListaTarefasDTO(repository.findByDataEventoBetween(dataInicial, dataFinal));
    }

    public List<TarefasDTO> buscarTarefasPorEmailUsuario(String token){
        String emailUsuario = jwtUtil.extrairEmailToken(token.substring(7));

        return mapper.paraListaTarefasDTO(repository.findByEmailUsuario(emailUsuario));
    }
}
