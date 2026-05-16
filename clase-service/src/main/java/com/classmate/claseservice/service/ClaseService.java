package com.classmate.claseservice.service;

import com.classmate.claseservice.exception.ResourceNotFoundException;
import com.classmate.claseservice.client.ProfesorClient;
import com.classmate.claseservice.dto.ClaseRequest;
import com.classmate.claseservice.dto.ClaseResponse;
import com.classmate.claseservice.entity.Clase;
import com.classmate.claseservice.repository.ClaseRepository;
import feign.FeignException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClaseService {

    private final ClaseRepository claseRepository;

    private final ProfesorClient profesorClient;

    public ClaseService(
            ClaseRepository claseRepository,
            ProfesorClient profesorClient){

        this.claseRepository = claseRepository;
        this.profesorClient = profesorClient;
    }

    public ClaseResponse crearClase(ClaseRequest request){

        validarProfesor(request.getProfesorId());
        Clase clase = new Clase();

        clase.setAsignatura(request.getAsignatura());
        clase.setDescripcion(request.getDescripcion());
        clase.setPrecio(request.getPrecio());
        clase.setFecha(request.getFecha());
        clase.setDuracion(request.getDuracion());
        clase.setProfesorId(request.getProfesorId());

        Clase guardada = claseRepository.save(clase);

        return new ClaseResponse(
                guardada.getId(),
                guardada.getAsignatura(),
                guardada.getDescripcion(),
                guardada.getPrecio(),
                guardada.getFecha(),
                guardada.getDuracion(),
                guardada.getProfesorId()
        );
    }

    public List<ClaseResponse> obtenerClases(){

        return claseRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }
    public ClaseResponse obtenerClasePorId(Long id){

        Clase clase = claseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Clase no encontrada"));

        return toResponse(clase);
    }

    public void eliminarClase(Long id){

        if (!claseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Clase no encontrada");
        }

        claseRepository.deleteById(id);
    }

    public ClaseResponse  actualizarClase(Long id, ClaseRequest request){

        validarProfesor(request.getProfesorId());
        Clase clase = claseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Clase no encontrada"));

        clase.setAsignatura(request.getAsignatura());
        clase.setDescripcion(request.getDescripcion());
        clase.setPrecio(request.getPrecio());
        clase.setFecha(request.getFecha());
        clase.setDuracion(request.getDuracion());
        clase.setProfesorId(request.getProfesorId());

        Clase actualizada = claseRepository.save(clase);

        return toResponse(actualizada);
    }

    private void validarProfesor(Long profesorId){

        try {

            profesorClient.buscarPorId(profesorId);

        } catch (FeignException.NotFound e){

            throw new ResourceNotFoundException(
                    "El profesor con id " + profesorId + " no existe");

        } catch (FeignException e){

            throw new RuntimeException(
                    "No se puede conectar con profesor-service");
        }
    }

    private ClaseResponse toResponse(Clase clase){

        return new ClaseResponse(
                clase.getId(),
                clase.getAsignatura(),
                clase.getDescripcion(),
                clase.getPrecio(),
                clase.getFecha(),
                clase.getDuracion(),
                clase.getProfesorId()
        );
    }

}
