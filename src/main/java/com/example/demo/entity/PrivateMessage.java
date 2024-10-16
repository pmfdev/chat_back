package com.example.demo.entity;

import lombok.Data;

@Data
public class PrivateMessage {
    private Long destinatarioId;
    private String contenido;

    // Getters y Setters
}
