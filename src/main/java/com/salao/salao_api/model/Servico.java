package com.salao.salao_api.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "servicos")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;             // ex: "Corte feminino"

    @Column(length = 255)
    private String descricao;

    @Column(nullable = false)
    private Integer duracaoMinutos;  // ex: 60 (para 1 hora)

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;        // BigDecimal para dinheiro — nunca use double!
}