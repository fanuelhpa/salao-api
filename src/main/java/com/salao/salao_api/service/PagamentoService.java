package com.salao.salao_api.service;

import com.salao.salao_api.dto.pagamento.PagamentoRequestDTO;
import com.salao.salao_api.dto.pagamento.PagamentoResponseDTO;
import com.salao.salao_api.exception.RecursoNaoEncontradoException;
import com.salao.salao_api.exception.RegraDeNegocioException;
import com.salao.salao_api.model.Agendamento;
import com.salao.salao_api.model.Agendamento.StatusAgendamento;
import com.salao.salao_api.model.Pagamento;
import com.salao.salao_api.repository.AgendamentoRepository;
import com.salao.salao_api.repository.PagamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final AgendamentoRepository agendamentoRepository;

    public List<PagamentoResponseDTO> listarTodos() {
        return pagamentoRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public PagamentoResponseDTO buscarPorId(Long id) {
        return toResponseDTO(buscarPagamento(id));
    }

    public PagamentoResponseDTO buscarPorAgendamento(Long agendamentoId) {
        Pagamento pagamento = pagamentoRepository.findByAgendamentoId(agendamentoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Pagamento não encontrado para o agendamento id: " + agendamentoId
                ));
        return toResponseDTO(pagamento);
    }

    public PagamentoResponseDTO registrar(PagamentoRequestDTO dto) {

        // 1. Verifica se o agendamento existe
        Agendamento agendamento = agendamentoRepository.findById(dto.agendamentoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Agendamento não encontrado com id: " + dto.agendamentoId()
                ));

        // 2. Regra: só pode pagar agendamento concluído
        if (agendamento.getStatus() != StatusAgendamento.CONCLUIDO) {
            throw new RegraDeNegocioException(
                    "Só é possível registrar pagamento para agendamentos concluídos."
            );
        }

        // 3. Regra: não pode pagar duas vezes
        if (pagamentoRepository.findByAgendamentoId(dto.agendamentoId()).isPresent()) {
            throw new RegraDeNegocioException(
                    "Já existe um pagamento registrado para este agendamento."
            );
        }

        // 4. Tudo ok — registra o pagamento
        Pagamento pagamento = new Pagamento();
        pagamento.setAgendamento(agendamento);
        pagamento.setValor(dto.valor());
        pagamento.setMetodoPagamento(dto.metodoPagamento());
        pagamento.setDataPagamento(LocalDateTime.now());

        return toResponseDTO(pagamentoRepository.save(pagamento));
    }

    private Pagamento buscarPagamento(Long id) {
        return pagamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Pagamento não encontrado com id: " + id
                ));
    }

    private PagamentoResponseDTO toResponseDTO(Pagamento pagamento) {
        return new PagamentoResponseDTO(
                pagamento.getId(),
                pagamento.getAgendamento().getId(),
                pagamento.getAgendamento().getCliente().getNome(),
                pagamento.getAgendamento().getServico().getNome(),
                pagamento.getValor(),
                pagamento.getMetodoPagamento(),
                pagamento.getDataPagamento()
        );
    }
}