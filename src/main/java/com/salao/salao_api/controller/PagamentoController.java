package com.salao.salao_api.controller;

import com.salao.salao_api.dto.pagamento.PagamentoRequestDTO;
import com.salao.salao_api.dto.pagamento.PagamentoResponseDTO;
import com.salao.salao_api.service.PagamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @GetMapping
    public ResponseEntity<List<PagamentoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(pagamentoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pagamentoService.buscarPorId(id));
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<PagamentoResponseDTO>> buscarPorPeriodo(
            @RequestParam LocalDateTime inicio,
            @RequestParam LocalDateTime fim) {
        return ResponseEntity.ok(pagamentoService.buscarPorPeriodo(inicio, fim));
    }

    @GetMapping("/agendamento/{agendamentoId}")
    public ResponseEntity<PagamentoResponseDTO> buscarPorAgendamento(@PathVariable Long agendamentoId) {
        return ResponseEntity.ok(pagamentoService.buscarPorAgendamento(agendamentoId));
    }

    @PostMapping
    public ResponseEntity<PagamentoResponseDTO> registrar(@Valid @RequestBody PagamentoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagamentoService.registrar(dto));
    }
}