package com.salao.salao_api.dto.auth;

public record LoginRequestDTO(
        String email,
        String senha
) {}