package com.salao.salao_api.service;

import com.salao.salao_api.dto.cliente.ClienteRequestDTO;
import com.salao.salao_api.dto.cliente.ClienteResponseDTO;
import com.salao.salao_api.exception.RecursoNaoEncontradoException;
import com.salao.salao_api.exception.RegraDeNegocioException;
import com.salao.salao_api.model.Cliente;
import com.salao.salao_api.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public List<ClienteResponseDTO> listarTodos() {
        return clienteRepository.findAll()
                .stream()
                .map(this::toResponseDTO)  // converte cada Cliente em ClienteResponseDTO
                .toList();
    }

    public ClienteResponseDTO buscarPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Cliente não encontrado com id: " + id
                ));
        return toResponseDTO(cliente);
    }

    public ClienteResponseDTO criar(ClienteRequestDTO dto) {
        if (clienteRepository.findByEmail(dto.email()).isPresent()) {
            throw new RegraDeNegocioException(
                    "Já existe um cliente com o e-mail: " + dto.email()
            );
        }

        Cliente cliente = new Cliente();
        cliente.setNome(dto.nome());
        cliente.setEmail(dto.email());
        cliente.setTelefone(dto.telefone());

        return toResponseDTO(clienteRepository.save(cliente));
    }

    public ClienteResponseDTO atualizar(Long id, ClienteRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Cliente não encontrado com id: " + id
                ));

        cliente.setNome(dto.nome());
        cliente.setEmail(dto.email());
        cliente.setTelefone(dto.telefone());

        return toResponseDTO(clienteRepository.save(cliente));
    }

    public void deletar(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException(
                    "Cliente não encontrado com id: " + id
            );
        }
        clienteRepository.deleteById(id);
    }

    // Método privado — converte entidade Cliente em ClienteResponseDTO
    // "private" porque só o próprio service precisa usar
    private ClienteResponseDTO toResponseDTO(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getTelefone(),
                cliente.getDataCadastro()
        );
    }
}