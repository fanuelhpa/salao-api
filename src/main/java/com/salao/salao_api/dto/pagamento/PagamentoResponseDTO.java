package com.salao.salao_api.dto.pagamento;

import com.salao.salao_api.model.Pagamento.MetodoPagamento;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PagamentoResponseDTO(
        Long id,
        Long agendamentoId,
        String clienteNome,
        String servicoNome,
        BigDecimal valor,
        MetodoPagamento metodoPagamento,
        LocalDateTime dataPagamento
) {}