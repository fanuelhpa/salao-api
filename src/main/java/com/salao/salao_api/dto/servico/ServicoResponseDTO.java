package com.salao.salao_api.dto.servico;

import java.math.BigDecimal;

public record ServicoResponseDTO(
        Long id,
        String nome,
        String descricao,
        Integer duracaoMinutos,
        BigDecimal preco
) {}