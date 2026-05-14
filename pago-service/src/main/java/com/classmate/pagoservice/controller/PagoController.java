package com.classmate.pagoservice.controller;

import com.classmate.pagoservice.entity.EstadoPago;
import com.classmate.pagoservice.entity.Pago;
import com.classmate.pagoservice.repository.PagoRepository;
import com.classmate.pagoservice.service.PagoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService){
        this.pagoService = pagoService;
    }

    @PostMapping
    public ResponseEntity<List<Pago>> obtenerPago(){
        return ResponseEntity.ok(pagoService.obtenerPagos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> obtenerPagoPorId(@PathVariable Long id){
        return ResponseEntity.ok(pagoService.obtenerPagoPorId(id));
    }

    @PutMapping("/{id}/aprobar")
    public ResponseEntity<Pago> aprobarPago(@PathVariable Long id){
        return ResponseEntity.ok(pagoService.aprobarPago(id));
    }

    @PutMapping("/{id}/rechazar")
    public ResponseEntity<Pago> rechazarPago(@PathVariable Long id){
        return ResponseEntity.ok(pagoService.rechazarPago(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPago(@PathVariable Long id){
        pagoService.eliminarPago(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Pago>> buscarPorEstado(@PathVariable EstadoPago estado){
        return ResponseEntity.ok(pagoService.buscarPorEstado(estado));
    }


}
