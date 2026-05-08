package com.salao.salao_api.service;

import com.salao.salao_api.dto.auth.LoginRequestDTO;
import com.salao.salao_api.dto.auth.LoginResponseDTO;
import com.salao.salao_api.exception.RegraDeNegocioException;
import com.salao.salao_api.model.Usuario;
import com.salao.salao_api.repository.UsuarioRepository;
import com.salao.salao_api.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponseDTO login(LoginRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.email())
                .orElseThrow(() -> new RegraDeNegocioException("Email ou senha inválidos."));

        if (!passwordEncoder.matches(dto.senha(), usuario.getSenha())) {
            throw new RegraDeNegocioException("Email ou senha inválidos.");
        }

        String token = jwtUtil.gerarToken(usuario.getEmail());
        return new LoginResponseDTO(token);
    }

    public void registrar(LoginRequestDTO dto) {
        if (usuarioRepository.findByEmail(dto.email()).isPresent()) {
            throw new RegraDeNegocioException("Já existe um usuário com esse email.");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(dto.email());
        usuario.setSenha(passwordEncoder.encode(dto.senha())); // sempre criptografa a senha!

        usuarioRepository.save(usuario);
    }
}