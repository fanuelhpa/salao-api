package com.salao.salao_api.repository;

import com.salao.salao_api.model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {

    // Busca serviços por nome (ignora maiúsculas/minúsculas)
    List<Servico> findByNomeContainingIgnoreCase(String nome);

    //Procura se existe um serviço passando o nome do serviço
    Boolean existsByNome(String nome);

    // Busca serviços até um preço máximo — útil para filtros
    List<Servico> findByPrecoLessThanEqual(BigDecimal precoMaximo);
}