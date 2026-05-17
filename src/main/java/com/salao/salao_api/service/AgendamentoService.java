package com.salao.salao_api.service;

import com.salao.salao_api.dto.agendamento.AgendamentoRequestDTO;
import com.salao.salao_api.dto.agendamento.AgendamentoResponseDTO;
import com.salao.salao_api.exception.RecursoNaoEncontradoException;
import com.salao.salao_api.exception.RegraDeNegocioException;
import com.salao.salao_api.model.Agendamento;
import com.salao.salao_api.model.Agendamento.StatusAgendamento;
import com.salao.salao_api.model.Cliente;
import com.salao.salao_api.model.Servico;
import com.salao.salao_api.repository.AgendamentoRepository;
import com.salao.salao_api.repository.ClienteRepository;
import com.salao.salao_api.repository.ServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final ClienteRepository clienteRepository;
    private final ServicoRepository servicoRepository;

    public List<AgendamentoResponseDTO> listarTodos() {
        return agendamentoRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public AgendamentoResponseDTO buscarPorId(Long id) {
        return toResponseDTO(buscarAgendamento(id));
    }

    public List<AgendamentoResponseDTO> listarPorCliente(Long clienteId) {
        return agendamentoRepository.findByClienteId(clienteId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public AgendamentoResponseDTO criar(AgendamentoRequestDTO dto) {

        // 1. Verifica se cliente e serviço existem
        Cliente cliente = clienteRepository.findById(dto.clienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Cliente não encontrado com id: " + dto.clienteId()
                ));

        Servico servico = servicoRepository.findById(dto.servicoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Serviço não encontrado com id: " + dto.servicoId()
                ));

        // 2. Regra: verifica conflito de horário
        LocalDateTime fimDoNovoServico = dto.dataHora().plusMinutes(servico.getDuracaoMinutos());

        List<Agendamento> agendadosEConcluidos = agendamentoRepository.findByStatusNot(StatusAgendamento.CANCELADO);

        for (Agendamento agendado : agendadosEConcluidos) {
            LocalDateTime fimDoAgendado = agendado.getDataHora()
                    .plusMinutes(agendado.getServico().getDuracaoMinutos());

            boolean haConflito =
                    dto.dataHora().isBefore(fimDoAgendado) &&
                            fimDoNovoServico.isAfter(agendado.getDataHora());

            if (haConflito) {
                throw new RegraDeNegocioException(
                        "Já existe um agendamento nesse horário ou um agendamento já concluído. Escolha outro horário."
                );
            }
        }

        // 3. Tudo ok — cria e salva
        Agendamento agendamento = new Agendamento();
        agendamento.setCliente(cliente);
        agendamento.setServico(servico);
        agendamento.setDataHora(dto.dataHora());
        agendamento.setObservacoes(dto.observacoes());
        agendamento.setStatus(StatusAgendamento.AGENDADO);

        return toResponseDTO(agendamentoRepository.save(agendamento));
    }

    public AgendamentoResponseDTO atualizar(Long id, AgendamentoRequestDTO dto) {

        Agendamento agendamento = buscarAgendamento(id);

        if (agendamento.getStatus() != StatusAgendamento.AGENDADO) {
            throw new RegraDeNegocioException(
                    "Apenas agendamentos com status AGENDADO podem ser editados."
            );
        }

        Servico servico = servicoRepository.findById(dto.servicoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Servico nao encontrado com id: " + dto.servicoId()
                ));

        // Verifica conflito de horario ignorando o proprio agendamento
        LocalDateTime fimDoNovoServico = dto.dataHora().plusMinutes(servico.getDuracaoMinutos());

        List<Agendamento> agendados = agendamentoRepository.findByStatusNot(StatusAgendamento.CANCELADO)
                .stream()
                .filter(a -> !a.getId().equals(id)) // ignora o proprio agendamento
                .toList();

        for (Agendamento agendado : agendados) {
            LocalDateTime fimDoAgendado = agendado.getDataHora()
                    .plusMinutes(agendado.getServico().getDuracaoMinutos());

            boolean haConflito =
                    dto.dataHora().isBefore(fimDoAgendado) &&
                            fimDoNovoServico.isAfter(agendado.getDataHora());

            if (haConflito) {
                throw new RegraDeNegocioException(
                        "Ja existe um agendamento nesse horario. Escolha outro horario."
                );
            }
        }

        agendamento.setServico(servico);
        agendamento.setDataHora(dto.dataHora());

        return toResponseDTO(agendamentoRepository.save(agendamento));
    }

    public AgendamentoResponseDTO cancelar(Long id) {
        Agendamento agendamento = buscarAgendamento(id);

        if (agendamento.getStatus() == StatusAgendamento.CONCLUIDO) {
            throw new RegraDeNegocioException(
                    "Não é possível cancelar um agendamento já concluído."
            );
        }

        agendamento.setStatus(StatusAgendamento.CANCELADO);
        return toResponseDTO(agendamentoRepository.save(agendamento));
    }

    public AgendamentoResponseDTO concluir(Long id) {
        Agendamento agendamento = buscarAgendamento(id);

        if (agendamento.getStatus() != StatusAgendamento.AGENDADO) {
            throw new RegraDeNegocioException(
                    "Apenas agendamentos com status AGENDADO podem ser concluídos."
            );
        }

        // Calcula o horário de término do serviço
        LocalDateTime fimDoServico = agendamento.getDataHora()
                .plusMinutes(agendamento.getServico().getDuracaoMinutos());

        // Só permite concluir após o término do serviço
        if (LocalDateTime.now().isBefore(fimDoServico)) {
            throw new RegraDeNegocioException(
                    "Este agendamento só pode ser concluído após o término do serviço, em "
                            + fimDoServico.toLocalDate().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                            + " às "
                            + fimDoServico.toLocalTime().toString().substring(0, 5) + "."
            );
        }

        agendamento.setStatus(StatusAgendamento.CONCLUIDO);
        return toResponseDTO(agendamentoRepository.save(agendamento));
    }

    public void deletar(Long id) {
        buscarAgendamento(id);
        agendamentoRepository.deleteById(id);
    }

    // Busca a entidade pura — usado internamente pelos outros métodos
    private Agendamento buscarAgendamento(Long id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Agendamento não encontrado com id: " + id
                ));
    }

    public List<AgendamentoResponseDTO> listarPorData(LocalDate data) {
        LocalDateTime inicio = data.atStartOfDay();
        LocalDateTime fim = data.atTime(23, 59, 59);
        return agendamentoRepository
                .findByDataHoraBetween(inicio, fim)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    // Converte entidade → DTO de resposta
    private AgendamentoResponseDTO toResponseDTO(Agendamento agendamento) {
        return new AgendamentoResponseDTO(
                agendamento.getId(),
                agendamento.getCliente().getId(),
                agendamento.getCliente().getNome(),
                agendamento.getServico().getId(),
                agendamento.getServico().getNome(),
                agendamento.getServico().getDuracaoMinutos(),
                agendamento.getDataHora(),
                agendamento.getStatus(),
                agendamento.getObservacoes()
        );
    }
}