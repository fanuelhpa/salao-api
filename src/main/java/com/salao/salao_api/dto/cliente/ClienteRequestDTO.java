package com.salao.salao_api.dto.cliente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ClienteRequestDTO(

        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
        String nome,

        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "Informe um e-mail válido.")
        @Pattern(
                regexp = "^[^@]+@[^@]+\\.[^@]+$",
                message = "Informe um e-mail válido com domínio completo (ex: nome@email.com)."
        )
        @Size(max = 100, message = "O e-mail deve ter no máximo 100 caracteres.")
        String email,

        @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres.")
        String telefone
) {}