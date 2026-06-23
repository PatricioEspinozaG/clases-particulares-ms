package com.classmate.profesorservice.service;

import com.classmate.profesorservice.client.UsuarioClient;
import com.classmate.profesorservice.dto.ProfesorRequest;
import com.classmate.profesorservice.dto.ProfesorResponse;
import com.classmate.profesorservice.entity.Profesor;
import com.classmate.profesorservice.exception.ResourceNotFoundException;
import com.classmate.profesorservice.repository.ProfesorRepository;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProfesorService {

    private final ProfesorRepository profesorRepository;
    private final UsuarioClient usuarioClient;

    public ProfesorService(
            ProfesorRepository profesorRepository,
            UsuarioClient usuarioClient) {

        this.profesorRepository = profesorRepository;
        this.usuarioClient = usuarioClient;
    }

    public ProfesorResponse crear(ProfesorRequest request) {

        log.info("Creando profesor para usuario {}",
                request.getUsuarioId());

        validarUsuario(request.getUsuarioId());

        if (profesorRepository.findByUsuarioId(request.getUsuarioId()).isPresent()) {

            log.error("El usuario {} ya está registrado como profesor",
                    request.getUsuarioId());

            throw new RuntimeException(
                    "El usuario ya está registrado como profesor");
        }

        Profesor profesor = new Profesor();

        profesor.setUsuarioId(request.getUsuarioId());
        profesor.setEspecialidad(request.getEspecialidad());
        profesor.setDescripcion(request.getDescripcion());
        profesor.setPrecioHora(request.getPrecioHora());
        profesor.setExperienciaAnios(request.getExperienciaAnios());
        profesor.setEstado(request.getEstado());

        Profesor guardado = profesorRepository.save(profesor);

        log.info("Profesor creado correctamente con id {}",
                guardado.getId());

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
                .orElseThrow(() ->
                        new ResourceNotFoundException("Profesor no encontrado"));

        return toResponse(profesor);
    }

    public ProfesorResponse buscarPorUsuarioId(Long usuarioId) {

        Profesor profesor = profesorRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Profesor no encontrado"));

        return toResponse(profesor);
    }

    public List<ProfesorResponse> buscarPorEspecialidad(String especialidad) {

        return profesorRepository
                .findByEspecialidadContainingIgnoreCase(especialidad)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ProfesorResponse actualizar(Long id, ProfesorRequest request) {

        log.info("Actualizando profesor con id {}", id);

        validarUsuario(request.getUsuarioId());

        Profesor profesor = profesorRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Profesor no encontrado"));

        profesor.setUsuarioId(request.getUsuarioId());
        profesor.setEspecialidad(request.getEspecialidad());
        profesor.setDescripcion(request.getDescripcion());
        profesor.setPrecioHora(request.getPrecioHora());
        profesor.setExperienciaAnios(request.getExperienciaAnios());
        profesor.setEstado(request.getEstado());

        Profesor actualizado = profesorRepository.save(profesor);

        log.info("Profesor {} actualizado correctamente",
                actualizado.getId());

        return toResponse(actualizado);
    }

    public void eliminar(Long id) {

        log.info("Eliminando profesor con id {}", id);

        if (!profesorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Profesor no encontrado");
        }

        profesorRepository.deleteById(id);

        log.info("Profesor {} eliminado correctamente", id);
    }

    private void validarUsuario(Long usuarioId) {

        log.info("Validando usuario con id {}",
                usuarioId);

        try {

            usuarioClient.buscarPorId(usuarioId);

        } catch (FeignException.NotFound e) {

            log.error("Usuario {} no existe",
                    usuarioId);

            throw new ResourceNotFoundException(
                    "El usuario con id " + usuarioId + " no existe");

        } catch (FeignException e) {

            log.error("Error conectando con usuario-service");

            throw new RuntimeException(
                    "No se puede conectar con usuario-service");
        }
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