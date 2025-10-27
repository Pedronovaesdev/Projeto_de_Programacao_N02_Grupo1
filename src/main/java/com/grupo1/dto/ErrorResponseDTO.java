package com.grupo1.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponseDTO {
    private String mensagem;
    private LocalDateTime timeStamp;

    public ErrorResponseDTO(String mensagem, LocalDateTime timeStamp) {
        this.mensagem = mensagem;
        this.timeStamp = timeStamp;
    }
}
