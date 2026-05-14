package com.classmate.claseservice.service;

import com.classmate.claseservice.dto.ClaseRequest;
import com.classmate.claseservice.dto.ClaseResponse;
import com.classmate.claseservice.entity.Clase;
import com.classmate.claseservice.repository.ClaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClaseService {

    private final ClaseRepository claseRepository;

    public ClaseService(ClaseRepository claseRepository){
        this.claseRepository = claseRepository;
    }

    public ClaseResponse crearClase(ClaseRequest request){

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

    public List<Clase> obtenerClases(){
        return claseRepository.findAll();
    }

    public Clase obtenerClasePorId(Long id){
        return claseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada"));
    }

    public void eliminarClase(Long id){
        claseRepository.deleteById(id);
    }

    public Clase actualizarClase(Long id, ClaseRequest request){

        Clase clase = claseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada"));

        clase.setAsignatura(request.getAsignatura());
        clase.setDescripcion(request.getDescripcion());
        clase.setPrecio(request.getPrecio());
        clase.setFecha(request.getFecha());
        clase.setDuracion(request.getDuracion());
        clase.setProfesorId(request.getProfesorId());

        return claseRepository.save(clase);
    }




}
