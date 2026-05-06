package com.salao.salao_api.service;

import com.salao.salao_api.dto.servico.ServicoRequestDTO;
import com.salao.salao_api.dto.servico.ServicoResponseDTO;
import com.salao.salao_api.exception.RecursoNaoEncontradoException;
import com.salao.salao_api.exception.RegraDeNegocioException;
import com.salao.salao_api.model.Servico;
import com.salao.salao_api.repository.AgendamentoRepository;
import com.salao.salao_api.repository.ServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicoService {

    private final ServicoRepository servicoRepository;

    @Autowired
    private final AgendamentoRepository agendamentoRepository;

    public List<ServicoResponseDTO> listarTodos() {
        return servicoRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public ServicoResponseDTO buscarPorId(Long id) {
        return toResponseDTO(buscarServico(id));
    }

    public ServicoResponseDTO criar(ServicoRequestDTO dto) {
        if (servicoRepository.existsByNome(dto.nome())) {
            throw new RegraDeNegocioException(
                    "Já existe um serviço com o nome: " + dto.nome()
            );
        }

        Servico servico = new Servico();
        servico.setNome(dto.nome());
        servico.setDescricao(dto.descricao());
        servico.setDuracaoMinutos(dto.duracaoMinutos());
        servico.setPreco(dto.preco());

        return toResponseDTO(servicoRepository.save(servico));
    }

    public ServicoResponseDTO atualizar(Long id, ServicoRequestDTO dto) {
        Servico servico = buscarServico(id);

        servico.setNome(dto.nome());
        servico.setDescricao(dto.descricao());
        servico.setDuracaoMinutos(dto.duracaoMinutos());
        servico.setPreco(dto.preco());

        return toResponseDTO(servicoRepository.save(servico));
    }

    public void deletar(Long id) {
        buscarServico(id); // já lança exceção se não existir

        if (agendamentoRepository.existsByServicoId(id)) {
            throw new RegraDeNegocioException(
                    "Não é possível excluir o serviço pois existem agendamentos vinculados a ele."
            );
        }

        servicoRepository.deleteById(id);
    }

    private Servico buscarServico(Long id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Serviço não encontrado com id: " + id
                ));
    }

    private ServicoResponseDTO toResponseDTO(Servico servico) {
        return new ServicoResponseDTO(
                servico.getId(),
                servico.getNome(),
                servico.getDescricao(),
                servico.getDuracaoMinutos(),
                servico.getPreco()
        );
    }
}