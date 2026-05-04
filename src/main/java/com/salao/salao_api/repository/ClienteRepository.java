package com.salao.salao_api.repository;

import com.salao.salao_api.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    //                                                   ↑ entidade  ↑ tipo do ID

    // Spring gera o SQL automaticamente pelo nome do método:
    // SELECT * FROM clientes WHERE email = ?
    Optional<Cliente> findByEmail(String email);

    // SELECT * FROM clientes WHERE telefone = ?
    Optional<Cliente> findByTelefone(String telefone);

    // SELECT * FROM clientes WHERE nome LIKE %nome%
    List<Cliente> findByNomeContainingIgnoreCase(String nome);
}