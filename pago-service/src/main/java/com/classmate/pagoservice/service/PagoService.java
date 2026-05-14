package com.classmate.pagoservice.service;

import com.classmate.pagoservice.dto.PagoRequest;
import com.classmate.pagoservice.dto.PagoResponse;
import com.classmate.pagoservice.entity.EstadoPago;
import com.classmate.pagoservice.entity.Pago;
import com.classmate.pagoservice.repository.PagoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;

    public PagoService(PagoRepository pagoRepository){
        this.pagoRepository = pagoRepository;
    }

    public Pago crearPago(PagoRequest request){

        Pago pago = new Pago();

        pago.setReservaId(request.getReservaId());
        pago.setMonto(request.getMonto());
        pago.setMetodoPago(request.getMetodoPago());
        pago.setEstado(EstadoPago.PENDIENTE);
        pago.setFechaPago(LocalDateTime.now());

        return pagoRepository.save(pago);
    }

    public List<Pago> obtenerPagos(){
        return pagoRepository.findAll();
    }

    public Pago obtenerPagoPorId(Long id){
        return pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
    }

    public Pago aprobarPago(Long id){

        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        pago.setEstado(EstadoPago.APROBADO);

        return pagoRepository.save(pago);
    }

    public Pago rechazarPago(Long id){

        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        pago.setEstado(EstadoPago.RECHAZADO);

        return pagoRepository.save(pago);
    }

    public void eliminarPago(Long id){
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        pagoRepository.delete(pago);
    }

    public List<Pago> buscarPorEstado(EstadoPago estado){
        return pagoRepository.findByEstado(estado);
    }
}
