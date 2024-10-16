package com.example.demo.repository;

import com.example.demo.entity.Mensaje;
import com.example.demo.entity.Sala;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MensajeRepository extends JpaRepository<Mensaje, Long> {
    List<Mensaje> findBySalaOrderByTimestampAsc(Sala sala);
}
