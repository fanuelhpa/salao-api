package com.salao.salao_api.dto.cliente;

public record ClienteRequestDTO(
        String nome,
        String email,
        String telefone
) {}