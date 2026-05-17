package com.classmate.pagoservice.service;

import com.classmate.pagoservice.client.ReservaClient;
import com.classmate.pagoservice.dto.PagoRequest;
import com.classmate.pagoservice.dto.PagoResponse;
import com.classmate.pagoservice.entity.EstadoPago;
import com.classmate.pagoservice.entity.Pago;
import com.classmate.pagoservice.exception.ResourceNotFoundException;
import com.classmate.pagoservice.repository.PagoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final ReservaClient reservaClient;

    public PagoService(PagoRepository pagoRepository, ReservaClient reservaClient) {
        this.pagoRepository = pagoRepository;
        this.reservaClient = reservaClient;
    }

    public PagoResponse crearPago(PagoRequest request) {

        reservaClient.obtenerReservaPorId(request.getReservaId());

        Pago pago = new Pago();

        pago.setReservaId(request.getReservaId());
        pago.setMonto(request.getMonto());
        pago.setMetodoPago(request.getMetodoPago());
        pago.setEstado(EstadoPago.PENDIENTE);
        pago.setFechaPago(LocalDateTime.now());

        Pago guardado = pagoRepository.save(pago);

        return toResponse(guardado);
    }

    public List<PagoResponse> obtenerPagos() {

        return pagoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PagoResponse obtenerPagoPorId(Long id) {

        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Pago no encontrado"));

        return toResponse(pago);
    }

    public PagoResponse aprobarPago(Long id) {

        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Pago no encontrado"));

        pago.setEstado(EstadoPago.APROBADO);

        Pago actualizado = pagoRepository.save(pago);

        return toResponse(actualizado);
    }

    public PagoResponse rechazarPago(Long id) {

        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Pago no encontrado"));

        pago.setEstado(EstadoPago.RECHAZADO);

        Pago actualizado = pagoRepository.save(pago);

        return toResponse(actualizado);
    }

    public void eliminarPago(Long id) {

        if (!pagoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pago no encontrado");
        }

        pagoRepository.deleteById(id);
    }

    public List<PagoResponse> buscarPorEstado(EstadoPago estado) {

        return pagoRepository.findByEstado(estado)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private PagoResponse toResponse(Pago pago) {

        return new PagoResponse(
                pago.getId(),
                pago.getReservaId(),
                pago.getMonto(),
                pago.getMetodoPago(),
                pago.getEstado(),
                pago.getFechaPago()
        );
    }
}
