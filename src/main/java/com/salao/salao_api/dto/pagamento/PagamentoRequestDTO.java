package com.salao.salao_api.dto.pagamento;

import com.salao.salao_api.model.Pagamento.MetodoPagamento;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PagamentoRequestDTO(

        @NotNull(message = "O agendamento é obrigatório.")
        Long agendamentoId,

        @NotNull(message = "O valor é obrigatório.")
        @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero.")
        BigDecimal valor,

        @NotNull(message = "O método de pagamento é obrigatório.")
        MetodoPagamento metodoPagamento
) {}