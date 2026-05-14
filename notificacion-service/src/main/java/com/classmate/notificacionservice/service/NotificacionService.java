package com.classmate.notificacionservice.service;

import com.classmate.notificacionservice.dto.NotificacionRequest;
import org.springframework.stereotype.Service;

@Service
public class NotificacionService {

    public String enviarEmail(NotificacionRequest request) {

        System.out.println("===== EMAIL ENVIADO =====");
        System.out.println("Destinatario: " + request.getDestinatario());
        System.out.println("Asunto: " + request.getAsunto());
        System.out.println("Mensaje: " + request.getMensaje());

        return "Correo enviado correctamente";
    }

    public String enviarNotificacionPago(NotificacionRequest request) {

        System.out.println("===== NOTIFICACION DE PAGO =====");
        System.out.println("Destinatario: " + request.getDestinatario());
        System.out.println("Mensaje: " + request.getMensaje());

        return "Notificación de pago enviada";
    }

    public String enviarNotificacionReserva(NotificacionRequest request) {

        System.out.println("===== NOTIFICACION DE RESERVA =====");
        System.out.println("Destinatario: " + request.getDestinatario());
        System.out.println("Mensaje: " + request.getMensaje());

        return "Notificación de reserva enviada";
    }
}