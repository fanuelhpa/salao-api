package com.salao.salao_api.repository;

import com.salao.salao_api.model.Pagamento;
import com.salao.salao_api.model.Pagamento.MetodoPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    // Busca o pagamento de um agendamento específico
    Optional<Pagamento> findByAgendamentoId(Long agendamentoId);

    // Pagamentos por método (PIX, DINHEIRO, etc.)
    List<Pagamento> findByMetodoPagamento(MetodoPagamento metodo);

    // SOMA total de pagamentos num período — coração dos relatórios financeiros!
    @Query("SELECT SUM(p.valor) FROM Pagamento p " +
            "WHERE p.dataPagamento BETWEEN :inicio AND :fim")
    BigDecimal sumValorByPeriodo(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );
}