package com.classmate.profesorservice.repository;

import com.classmate.profesorservice.entity.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfesorRepository extends JpaRepository<Profesor, Long> {

    Optional<Profesor> findByUsuarioId(Long usuarioId);

    List<Profesor> findByEspecialidadContainingIgnoreCase(String especialidad);
}