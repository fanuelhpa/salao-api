package com.salao.salao_api.dto.agendamento;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AgendamentoRequestDTO(

        @NotNull(message = "O cliente é obrigatório.")
        Long clienteId,

        @NotNull(message = "O serviço é obrigatório.")
        Long servicoId,

        @NotNull(message = "A data e hora são obrigatórias.")
        //@Future(message = "A data do agendamento deve ser no futuro.")
        LocalDateTime dataHora,
        
        String observacoes
) {}