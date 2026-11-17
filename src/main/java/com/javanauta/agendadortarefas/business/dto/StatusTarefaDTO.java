package com.javanauta.agendadortarefas.business.dto;

import com.javanauta.agendadortarefas.infrastructure.enums.StatusNotificacaoEnum;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatusTarefaDTO {

    private String id;
    private StatusNotificacaoEnum statusNotificacaoEnum;

}
