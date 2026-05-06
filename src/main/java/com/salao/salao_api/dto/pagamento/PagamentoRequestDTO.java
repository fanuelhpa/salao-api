package com.salao.salao_api.dto.pagamento;

import com.salao.salao_api.model.Pagamento.MetodoPagamento;
import java.math.BigDecimal;

public record PagamentoRequestDTO(
        Long agendamentoId,
        BigDecimal valor,
        MetodoPagamento metodoPagamento
) {}