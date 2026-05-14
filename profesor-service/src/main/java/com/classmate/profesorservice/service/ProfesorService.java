package com.classmate.profesorservice.service;

import com.classmate.profesorservice.dto.ProfesorRequest;
import com.classmate.profesorservice.dto.ProfesorResponse;
import com.classmate.profesorservice.entity.Profesor;
import com.classmate.profesorservice.repository.ProfesorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfesorService {

    private final ProfesorRepository profesorRepository;

    public ProfesorService(ProfesorRepository profesorRepository) {
        this.profesorRepository = profesorRepository;
    }

    public ProfesorResponse crear(ProfesorRequest request) {

        if (profesorRepository.findByUsuarioId(request.getUsuarioId()).isPresent()) {
            throw new RuntimeException("El usuario ya está registrado como profesor");
        }

        Profesor profesor = new Profesor();
        profesor.setUsuarioId(request.getUsuarioId());
        profesor.setEspecialidad(request.getEspecialidad());
        profesor.setDescripcion(request.getDescripcion());
        profesor.setPrecioHora(request.getPrecioHora());
        profesor.setExperienciaAnios(request.getExperienciaAnios());
        profesor.setEstado(request.getEstado());

        Profesor guardado = profesorRepository.save(profesor);

        return toResponse(guardado);
    }

    public List<ProfesorResponse> listar() {
        return profesorRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ProfesorResponse buscarPorId(Long id) {
        Profesor profesor = profesorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profesor no encontrado"));

        return toResponse(profesor);
    }

    public ProfesorResponse buscarPorUsuarioId(Long usuarioId) {
        Profesor profesor = profesorRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Profesor no encontrado"));

        return toResponse(profesor);
    }

    public List<ProfesorResponse> buscarPorEspecialidad(String especialidad) {
        return profesorRepository.findByEspecialidadContainingIgnoreCase(especialidad)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ProfesorResponse actualizar(Long id, ProfesorRequest request) {
        Profesor profesor = profesorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profesor no encontrado"));

        profesor.setUsuarioId(request.getUsuarioId());
        profesor.setEspecialidad(request.getEspecialidad());
        profesor.setDescripcion(request.getDescripcion());
        profesor.setPrecioHora(request.getPrecioHora());
        profesor.setExperienciaAnios(request.getExperienciaAnios());
        profesor.setEstado(request.getEstado());

        Profesor actualizado = profesorRepository.save(profesor);

        return toResponse(actualizado);
    }

    public void eliminar(Long id) {
        if (!profesorRepository.existsById(id)) {
            throw new RuntimeException("Profesor no encontrado");
        }

        profesorRepository.deleteById(id);
    }

    private ProfesorResponse toResponse(Profesor profesor) {
        return new ProfesorResponse(
                profesor.getId(),
                profesor.getUsuarioId(),
                profesor.getEspecialidad(),
                profesor.getDescripcion(),
                profesor.getPrecioHora(),
                profesor.getExperienciaAnios(),
                profesor.getEstado()
        );
    }
}