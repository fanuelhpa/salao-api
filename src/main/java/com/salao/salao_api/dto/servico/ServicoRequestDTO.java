package com.salao.salao_api.dto.servico;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ServicoRequestDTO(

        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
        String nome,

        @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres.")
        String descricao,

        @NotNull(message = "A duração é obrigatória.")
        @Min(value = 1, message = "A duração deve ser de pelo menos 1 minuto.")
        Integer duracaoMinutos,

        @NotNull(message = "O preço é obrigatório.")
        @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero.")
        BigDecimal preco
) {}