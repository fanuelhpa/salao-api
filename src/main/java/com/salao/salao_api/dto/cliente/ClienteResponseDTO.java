package com.salao.salao_api.dto.cliente;

import java.time.LocalDateTime;

public record ClienteResponseDTO(
        Long id,
        String nome,
        String email,
        String telefone,
        LocalDateTime dataCadastro
) {}