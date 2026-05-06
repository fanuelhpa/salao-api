package com.salao.salao_api.exception.handler;

import com.salao.salao_api.dto.ErroResponseDTO;
import com.salao.salao_api.exception.RecursoNaoEncontradoException;
import com.salao.salao_api.exception.RegraDeNegocioException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Trata RecursoNaoEncontradoException → devolve 404
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponseDTO> handleRecursoNaoEncontrado(RecursoNaoEncontradoException ex) {
        ErroResponseDTO erro = new ErroResponseDTO(
                HttpStatus.NOT_FOUND.value(),  // 404
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    // Trata RegraDeNegocioException → devolve 422
    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<ErroResponseDTO> handleRegraDeNegocio(RegraDeNegocioException ex) {
        ErroResponseDTO erro = new ErroResponseDTO(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),  // 422
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(erro);
    }

    // Trata qualquer outra exceção não prevista → devolve 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponseDTO> handleErroGenerico(Exception ex) {
        ErroResponseDTO erro = new ErroResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),  // 500
                "Ocorreu um erro interno. Tente novamente mais tarde.",
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }
}