package com.salao.salao_api.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamentos")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Um pagamento pertence a um agendamento
    @OneToOne                               // 1 pagamento para 1 agendamento
    @JoinColumn(name = "agendamento_id", nullable = false)
    private Agendamento agendamento;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pagamento", nullable = false)
    private MetodoPagamento metodoPagamento;

    @Column(name = "data_pagamento")
    private LocalDateTime dataPagamento;

    public enum MetodoPagamento {
        DINHEIRO,
        PIX,
        CARTAO_DEBITO,
        CARTAO_CREDITO
    }
}