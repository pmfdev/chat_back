package com.example.demo.repository;

import com.example.demo.entity.Sala;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SalaRepository extends JpaRepository<Sala, Long> {
    Optional<Sala> findByNombreAndTipo(String nombre, String tipo);
}
