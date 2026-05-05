package com.salao.salao_api.dto.agendamento;

import java.time.LocalDateTime;

public record AgendamentoRequestDTO(
        Long clienteId,
        Long servicoId,
        LocalDateTime dataHora,
        String observacoes
) {}