package com.example.demo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "usuarios")
@Data // Genera getters, setters, equals, hashCode, y toString automáticamente
@NoArgsConstructor // Genera un constructor vacío
@AllArgsConstructor // Genera un constructor con todos los atributos
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, columnDefinition = "nvarchar(255)")
    private String username;

    @Column(nullable = false, unique = true, columnDefinition = "nvarchar(255)")
    private String email;

    @Column(nullable = false, columnDefinition = "nvarchar(255)")
    private String passwordHash;
}
