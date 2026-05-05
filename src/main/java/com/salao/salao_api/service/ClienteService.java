package com.salao.salao_api.service;

import com.salao.salao_api.exception.RecursoNaoEncontradoException;
import com.salao.salao_api.exception.RegraDeNegocioException;
import com.salao.salao_api.model.Cliente;
import com.salao.salao_api.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service                    // marca como componente de serviço do Spring
@RequiredArgsConstructor    // Lombok gera construtor com os campos final (injeção de dependência)
public class ClienteService {

    @Autowired
    private final ClienteRepository clienteRepository;
    //           ↑ o Spring injeta automaticamente — isso é Injeção de Dependência

    // Lista todos os clientes
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    // Busca um cliente pelo ID — lança exceção se não encontrar
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Cliente não encontrado com id: " + id
                ));
    }

    // Cria um novo cliente
    public Cliente criar(Cliente cliente) {
        // Regra de negócio: não pode cadastrar e-mail duplicado
        if (clienteRepository.findByEmail(cliente.getEmail()).isPresent()) {
            throw new RegraDeNegocioException(
                    "Já existe um cliente com o e-mail: " + cliente.getEmail()
            );
        }
        return clienteRepository.save(cliente);
    }

    // Atualiza um cliente existente
    public Cliente atualizar(Long id, Cliente clienteAtualizado) {
        // Primeiro verifica se existe (lança exceção se não existir)
        Cliente clienteExistente = buscarPorId(id);

        // Atualiza apenas os campos permitidos
        clienteExistente.setNome(clienteAtualizado.getNome());
        clienteExistente.setTelefone(clienteAtualizado.getTelefone());
        clienteExistente.setEmail(clienteAtualizado.getEmail());

        return clienteRepository.save(clienteExistente);
        // save() com ID existente faz UPDATE, sem ID faz INSERT
    }

    // Deleta um cliente
    public void deletar(Long id) {
        // Verifica se existe antes de deletar
        buscarPorId(id);
        clienteRepository.deleteById(id);
    }
}