package com.salao.salao_api.service;

import com.salao.salao_api.exception.RecursoNaoEncontradoException;
import com.salao.salao_api.exception.RegraDeNegocioException;
import com.salao.salao_api.model.Servico;
import com.salao.salao_api.repository.ServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicoService {

    @Autowired
    private final ServicoRepository servicoRepository;

    public List<Servico> listarTodos() {
        return servicoRepository.findAll();
    }

    public Servico buscarPorId(Long id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Serviço não encontrado com id: " + id
                ));
    }

    public Servico criar(Servico servico) {

        if (servicoRepository.existsByNome(servico.getNome())) {
            throw new RegraDeNegocioException(
                    "Já existe um serviço com o nome: " + servico.getNome()
            );
        }
        return servicoRepository.save(servico);
    }

    public Servico atualizar(Long id, Servico servicoAtualizado) {
        Servico existente = buscarPorId(id);

        existente.setNome(servicoAtualizado.getNome());
        existente.setDescricao(servicoAtualizado.getDescricao());
        existente.setDuracaoMinutos(servicoAtualizado.getDuracaoMinutos());
        existente.setPreco(servicoAtualizado.getPreco());

        return servicoRepository.save(existente);
    }

    public void deletar(Long id) {
        buscarPorId(id);
        servicoRepository.deleteById(id);
    }
}