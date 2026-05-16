package com.classmate.profesorservice.service;
import com.classmate.profesorservice.exception.ResourceNotFoundException;

import com.classmate.profesorservice.client.UsuarioClient;
import com.classmate.profesorservice.dto.ProfesorRequest;
import com.classmate.profesorservice.dto.ProfesorResponse;
import com.classmate.profesorservice.entity.Profesor;
import com.classmate.profesorservice.repository.ProfesorRepository;
import feign.FeignException;
import org.springframework.stereotype.Service;

import java.util.List;

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

        try {

            validarUsuario(request.getUsuarioId());

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

        } catch (RuntimeException e) {
            throw e;

        } catch (Exception e) {
            throw new RuntimeException("Error al crear profesor");
        }
    }

    public List<ProfesorResponse> listar() {

        try {

            return profesorRepository.findAll()
                    .stream()
                    .map(this::toResponse)
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException("Error al listar profesores");
        }
    }

    public ProfesorResponse buscarPorId(Long id) {

        try {

            Profesor profesor = profesorRepository.findById(id)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Profesor no encontrado"));

            return toResponse(profesor);

        } catch (RuntimeException e) {
            throw e;

        } catch (Exception e) {
            throw new RuntimeException("Error al buscar profesor");
        }
    }

    public ProfesorResponse buscarPorUsuarioId(Long usuarioId) {

        try {

            Profesor profesor = profesorRepository.findByUsuarioId(usuarioId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Profesor no encontrado"));

            return toResponse(profesor);

        } catch (RuntimeException e) {
            throw e;

        } catch (Exception e) {
            throw new RuntimeException("Error al buscar profesor");
        }
    }

    public List<ProfesorResponse> buscarPorEspecialidad(String especialidad) {

        try {

            return profesorRepository
                    .findByEspecialidadContainingIgnoreCase(especialidad)
                    .stream()
                    .map(this::toResponse)
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException("Error al buscar profesores");
        }
    }

    public ProfesorResponse actualizar(Long id, ProfesorRequest request) {

        try {

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

            return toResponse(actualizado);

        } catch (RuntimeException e) {
            throw e;

        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar profesor");
        }
    }

    public void eliminar(Long id) {

        try {

            if (!profesorRepository.existsById(id)) {
                throw new ResourceNotFoundException("Profesor no encontrado");
            }

            profesorRepository.deleteById(id);

        } catch (RuntimeException e) {
            throw e;

        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar profesor");
        }
    }

    private void validarUsuario(Long usuarioId) {

        try {

            usuarioClient.buscarPorId(usuarioId);

        } catch (FeignException.NotFound e) {

            throw new ResourceNotFoundException(
                    "El usuario con id " + usuarioId + " no existe");

        } catch (FeignException e) {

            throw new RuntimeException(
                    "No se puede conectar con usuario-service");

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error al validar usuario");
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