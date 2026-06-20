package com.classmate.notificacionservice.controller;

import com.classmate.notificacionservice.dto.NotificacionRequest;
import com.classmate.notificacionservice.dto.NotificacionResponse;
import com.classmate.notificacionservice.service.NotificacionService;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificacionControllerTest {

    @Mock
    private NotificacionService notificacionService;

    private NotificacionController notificacionController;
    private Faker faker;
    private NotificacionRequest request;

    @BeforeEach
    void setUp() {
        notificacionController = new NotificacionController(notificacionService);
        faker = new Faker();

        request = new NotificacionRequest();
        request.setDestinatario(faker.internet().emailAddress());
        request.setAsunto(faker.lorem().sentence(3));
        request.setMensaje(faker.lorem().sentence(8));
    }

    @Test
    void enviarEmailDebeRetornarOk() {
        when(notificacionService.enviarEmail(request))
                .thenReturn("Correo enviado correctamente");

        ResponseEntity<String> response = notificacionController.enviarEmail(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Correo enviado correctamente");
        verify(notificacionService).enviarEmail(request);
    }

    @Test
    void enviarNotificacionPagoDebeRetornarOk() {
        when(notificacionService.enviarNotificacionPago(request))
                .thenReturn("Notificación de pago enviada");

        ResponseEntity<String> response = notificacionController.enviarNotificacionPago(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Notificación de pago enviada");
        verify(notificacionService).enviarNotificacionPago(request);
    }

    @Test
    void enviarNotificacionReservaDebeRetornarOk() {
        when(notificacionService.enviarNotificacionReserva(request))
                .thenReturn("Notificación de reserva enviada");

        ResponseEntity<String> response = notificacionController.enviarNotificacionReserva(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Notificación de reserva enviada");
        verify(notificacionService).enviarNotificacionReserva(request);
    }

    @Test
    void obtenerNotificacionesDebeRetornarCollectionModelConLinks() {
        NotificacionResponse notificacionResponse = new NotificacionResponse(
                faker.number().randomNumber(),
                faker.internet().emailAddress(),
                faker.lorem().sentence(),
                LocalDateTime.now()
        );

        when(notificacionService.obtenerNotificaciones())
                .thenReturn(List.of(notificacionResponse));

        ResponseEntity<CollectionModel<EntityModel<NotificacionResponse>>> response =
                notificacionController.obtenerNotificaciones();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent()).hasSize(1);
        assertThat(response.getBody().getLink("self")).isPresent();
        assertThat(response.getBody().getLink("enviar-email")).isPresent();
        assertThat(response.getBody().getLink("notificar-pago")).isPresent();
        assertThat(response.getBody().getLink("notificar-reserva")).isPresent();
        verify(notificacionService).obtenerNotificaciones();
    }
}
