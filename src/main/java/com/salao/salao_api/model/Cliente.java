package com.salao.salao_api.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity                          // essa classe é uma tabela no banco
@Table(name = "clientes")        // nome da tabela
@Getter @Setter                  // Lombok gera getters e setters
@NoArgsConstructor               // Lombok gera construtor vazio (JPA exige)
@AllArgsConstructor              // Lombok gera construtor com todos os campos
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID automático: 1, 2, 3...
    private Long id;

    @Column(nullable = false, length = 100) // campo obrigatório, máximo 100 chars
    private String nome;

    @Column(nullable = false, unique = true, length = 100) // e-mail único no banco
    private String email;

    @Column(length = 20)
    private String telefone;

    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    // Executado automaticamente antes de salvar no banco
    @PrePersist
    public void prePersist() {
        this.dataCadastro = LocalDateTime.now();
    }
}