package com.salao.salao_api.repository;

import com.salao.salao_api.model.Agendamento;
import com.salao.salao_api.model.Agendamento.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    // Todos os agendamentos de um cliente específico
    List<Agendamento> findByClienteId(Long clienteId);

    // Todos os agendamentos de um serviço específico
    List<Agendamento> findByServicoId(Long servicoId);

    // Agendamentos por status (AGENDADO, CONCLUIDO, CANCELADO)
    List<Agendamento> findByStatus(StatusAgendamento status);

    // Agendamentos entre duas datas — essencial para relatórios!
    // Spring gera: SELECT * FROM agendamentos WHERE data_hora BETWEEN ? AND ?
    List<Agendamento> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);

    // Agendamentos de um cliente com status específico
    List<Agendamento> findByClienteIdAndStatus(Long clienteId, StatusAgendamento status);

    // Verifica se existe algum agendamento vinculado a um serviço
    boolean existsByServicoId(Long servicoId);

    // Verifica se existe algum cliente vinculado a um serviço
    boolean existsByClienteId(Long clienteId);

    // Query JPQL personalizada — conta agendamentos concluídos num período
    // JPQL usa o nome da CLASSE Java (Agendamento), não o nome da tabela SQL
    @Query("SELECT COUNT(a) FROM Agendamento a " +
            "WHERE a.dataHora BETWEEN :inicio AND :fim " +
            "AND a.status = 'CONCLUIDO'")
    Long countConcluidosByPeriodo(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );
}