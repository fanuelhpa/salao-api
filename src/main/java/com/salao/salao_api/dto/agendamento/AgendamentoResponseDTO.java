package com.salao.salao_api.dto.agendamento;

import com.salao.salao_api.model.Agendamento.StatusAgendamento;
import java.time.LocalDateTime;

public record AgendamentoResponseDTO(
        Long id,
        Long clienteId,
        String clienteNome,       // nome do cliente — mais útil que só o ID
        Long servicoId,
        String servicoNome,       // nome do serviço — mesma ideia
        Integer duracaoMinutos,
        LocalDateTime dataHora,
        StatusAgendamento status,
        String observacoes
) {}