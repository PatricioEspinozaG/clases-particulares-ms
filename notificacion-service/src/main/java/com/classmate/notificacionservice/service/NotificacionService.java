package com.classmate.notificacionservice.service;

import com.classmate.notificacionservice.dto.NotificacionRequest;
import com.classmate.notificacionservice.dto.NotificacionResponse;
import com.classmate.notificacionservice.entity.Notificacion;
import com.classmate.notificacionservice.repository.NotificacionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;

    public NotificacionService(
            NotificacionRepository notificacionRepository) {

        this.notificacionRepository = notificacionRepository;
    }

    public String enviarEmail(NotificacionRequest request) {

        log.info("Enviando email a {}",
                request.getDestinatario());

        guardarNotificacion(request);

        log.info("Email enviado correctamente");

        return "Correo enviado correctamente";
    }

    public String enviarNotificacionPago(NotificacionRequest request) {

        log.info("Enviando notificación de pago a {}",
                request.getDestinatario());

        guardarNotificacion(request);

        log.info("Notificación de pago enviada correctamente");

        return "Notificación de pago enviada";
    }

    public String enviarNotificacionReserva(NotificacionRequest request) {

        log.info("Enviando notificación de reserva a {}",
                request.getDestinatario());

        guardarNotificacion(request);

        log.info("Notificación de reserva enviada correctamente");

        return "Notificación de reserva enviada";
    }

    private void guardarNotificacion(NotificacionRequest request) {

        Notificacion notificacion = new Notificacion();

        notificacion.setDestinatario(request.getDestinatario());
        notificacion.setMensaje(request.getMensaje());
        notificacion.setFechaEnvio(LocalDateTime.now());

        notificacionRepository.save(notificacion);

        log.info("Notificación guardada en base de datos");
    }

    public List<NotificacionResponse> obtenerNotificaciones() {

        return notificacionRepository.findAll()
                .stream()
                .map(notificacion -> new NotificacionResponse(
                        notificacion.getId(),
                        notificacion.getDestinatario(),
                        notificacion.getMensaje(),
                        notificacion.getFechaEnvio()
                ))
                .toList();
    }
}