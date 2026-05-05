package com.salao.salao_api.service;

import com.salao.salao_api.exception.RecursoNaoEncontradoException;
import com.salao.salao_api.exception.RegraDeNegocioException;
import com.salao.salao_api.model.Agendamento;
import com.salao.salao_api.model.Agendamento.StatusAgendamento;
import com.salao.salao_api.model.Cliente;
import com.salao.salao_api.model.Servico;
import com.salao.salao_api.repository.AgendamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    @Autowired
    private final AgendamentoRepository agendamentoRepository;

    @Autowired
    private final ClienteService clienteService;   // reutiliza o service de cliente

    @Autowired
    private final ServicoService servicoService;   // reutiliza o service de serviço

    public List<Agendamento> listarTodos() {
        return agendamentoRepository.findAll();
    }

    public Agendamento buscarPorId(Long id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Agendamento não encontrado com id: " + id
                ));
    }

    public List<Agendamento> listarPorCliente(Long clienteId) {
        return agendamentoRepository.findByClienteId(clienteId);
    }

    public Agendamento criar(Long clienteId, Long servicoId, LocalDateTime dataHora) {

        // 1. Verifica se cliente e serviço existem (lança exceção se não)
        Cliente cliente = clienteService.buscarPorId(clienteId);
        Servico servico = servicoService.buscarPorId(servicoId);

        // 2. Regra de negócio: não pode agendar no passado
        if (dataHora.isBefore(LocalDateTime.now())) {
            throw new RegraDeNegocioException(
                    "Não é possível agendar em uma data passada."
            );
        }

        // 3. Regra de negócio: verifica conflito de horário
        // Calcula o fim do serviço (início + duração)
        LocalDateTime fimDoServico = dataHora.plusMinutes(servico.getDuracaoMinutos());

        List<Agendamento> agendadosList = agendamentoRepository.findByStatus(StatusAgendamento.AGENDADO);

        for(Agendamento agendado : agendadosList) {
            LocalDateTime dataHorafinalDoAgendado = agendado.getDataHora().plusMinutes(agendado.getServico().getDuracaoMinutos());
            if(dataHora.isAfter(agendado.getDataHora()) && dataHora.isBefore(dataHorafinalDoAgendado) ||
                    fimDoServico.isAfter(agendado.getDataHora()) && fimDoServico.isBefore(dataHorafinalDoAgendado)) {

                throw new RegraDeNegocioException(
                        "Já existe um agendamento nesse horário. Escolha outro horário."
                );
            }
        }

        /*
        // Busca agendamentos que se sobrepõem nesse período
        List<Agendamento> conflitos = agendamentoRepository
                .findByDataHoraBetween(dataHora.minusMinutes(servico.getDuracaoMinutos()), fimDoServico)
                .stream()
                .filter(a -> a.getStatus() != StatusAgendamento.CANCELADO) // ignora cancelados
                .toList();

        if (!conflitos.isEmpty()) {
            throw new RegraDeNegocioException(
                    "Já existe um agendamento nesse horário. Escolha outro horário."
            );
        }
        */

        // 4. Tudo ok — cria o agendamento
        Agendamento agendamento = new Agendamento();
        agendamento.setCliente(cliente);
        agendamento.setServico(servico);
        agendamento.setDataHora(dataHora);
        agendamento.setStatus(StatusAgendamento.AGENDADO);

        return agendamentoRepository.save(agendamento);
    }

    // Cancela um agendamento (não deleta — só muda o status)
    public Agendamento cancelar(Long id) {
        Agendamento agendamento = buscarPorId(id);

        if (agendamento.getStatus() == StatusAgendamento.CONCLUIDO) {
            throw new RegraDeNegocioException(
                    "Não é possível cancelar um agendamento já concluído."
            );
        }

        agendamento.setStatus(StatusAgendamento.CANCELADO);
        return agendamentoRepository.save(agendamento);
    }

    // Marca um agendamento como concluído
    public Agendamento concluir(Long id) {
        Agendamento agendamento = buscarPorId(id);

        if (agendamento.getStatus() != StatusAgendamento.AGENDADO) {
            throw new RegraDeNegocioException(
                    "Apenas agendamentos com status AGENDADO podem ser concluídos."
            );
        }

        agendamento.setStatus(StatusAgendamento.CONCLUIDO);
        return agendamentoRepository.save(agendamento);
    }

    public void deletar(Long id) {
        buscarPorId(id);
        agendamentoRepository.deleteById(id);
    }
}