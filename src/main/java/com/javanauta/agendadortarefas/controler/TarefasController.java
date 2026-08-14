package com.javanauta.agendadortarefas.controler;

import com.javanauta.agendadortarefas.business.TarefasService;
import com.javanauta.agendadortarefas.business.dto.TarefasDTO;
import com.javanauta.agendadortarefas.infrastructure.enums.StatusNotificacaoEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tarefas")
@RequiredArgsConstructor
public class TarefasController {

    private final TarefasService service;

    @PostMapping
    public ResponseEntity<TarefasDTO> gravarTarefas(@RequestBody TarefasDTO dto,
                                                    @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(service.gravarTarefa(token, dto));
    }

    @GetMapping("/eventos")
    public ResponseEntity<List<TarefasDTO>> buscarTarefasPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicial,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFinal) {

        return ResponseEntity.ok(service.buscaTarefasAgendadasPorPeriodo(dataInicial, dataFinal));
    }

    @GetMapping
    public ResponseEntity<List<TarefasDTO>> buscarTarefasPorEmail(@RequestHeader("Authorization") String token){
        return ResponseEntity.ok(service.buscarTarefasPorEmailUsuario(token));
    }

    @DeleteMapping
    public ResponseEntity<Void> deletaTarefaPorId(@RequestParam("id") String id){
        service.deletaTarefaPorID(id);

        return ResponseEntity.ok().build();
    }


    @PatchMapping
    public ResponseEntity<TarefasDTO> atualizarStatusTarefa(@RequestParam("status") StatusNotificacaoEnum status,
                                                            @RequestParam("id") String id){
        return ResponseEntity.ok(service.alterarStatus(status, id));
    }

    @PutMapping
    public ResponseEntity<TarefasDTO> updateTarefas(@RequestBody TarefasDTO dto,
                                                    @RequestParam("id") String id){
        return ResponseEntity.ok(service.updateTarefa(dto, id));
    }

}
