package com.salao.salao_api.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "agendamentos")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento: muitos agendamentos pertencem a um cliente
    // @JoinColumn define o nome da coluna de chave estrangeira na tabela
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    // Relacionamento: muitos agendamentos pertencem a um serviço
    @ManyToOne
    @JoinColumn(name = "servico_id", nullable = false)
    private Servico servico;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;  // ex: 2025-06-10T14:30:00

    @Enumerated(EnumType.STRING)     // salva o nome do enum no banco ("AGENDADO")
    @Column(nullable = false)
    private StatusAgendamento status;

    @Column(length = 255)
    private String observacoes;

    // Enum dentro da própria classe — status possíveis de um agendamento
    public enum StatusAgendamento {
        AGENDADO,
        CONCLUIDO,
        CANCELADO
    }
}