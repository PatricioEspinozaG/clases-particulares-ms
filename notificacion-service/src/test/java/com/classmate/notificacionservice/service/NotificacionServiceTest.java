package com.classmate.notificacionservice.service;

import com.classmate.notificacionservice.dto.NotificacionRequest;
import com.classmate.notificacionservice.dto.NotificacionResponse;
import com.classmate.notificacionservice.entity.Notificacion;
import com.classmate.notificacionservice.repository.NotificacionRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceTest {

    @Mock
    private NotificacionRepository notificacionRepository;

    @InjectMocks
    private NotificacionService notificacionService;

    private Faker faker;
    private NotificacionRequest request;

    @BeforeEach
    void setUp() {
        faker = new Faker();

        request = new NotificacionRequest();
        request.setDestinatario(faker.internet().emailAddress());
        request.setAsunto(faker.lorem().sentence(3));
        request.setMensaje(faker.lorem().sentence(8));
    }

    @Test
    void enviarEmailDebeGuardarNotificacionYRetornarMensaje() {
        when(notificacionRepository.save(any(Notificacion.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        String response = notificacionService.enviarEmail(request);

        ArgumentCaptor<Notificacion> captor = ArgumentCaptor.forClass(Notificacion.class);
        verify(notificacionRepository).save(captor.capture());

        Notificacion guardada = captor.getValue();
        assertThat(response).isEqualTo("Correo enviado correctamente");
        assertThat(guardada.getDestinatario()).isEqualTo(request.getDestinatario());
        assertThat(guardada.getMensaje()).isEqualTo(request.getMensaje());
        assertThat(guardada.getFechaEnvio()).isNotNull();
    }

    @Test
    void enviarNotificacionPagoDebeGuardarNotificacionYRetornarMensaje() {
        when(notificacionRepository.save(any(Notificacion.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        String response = notificacionService.enviarNotificacionPago(request);

        verify(notificacionRepository).save(any(Notificacion.class));
        assertThat(response).isEqualTo("Notificación de pago enviada");
    }

    @Test
    void enviarNotificacionReservaDebeGuardarNotificacionYRetornarMensaje() {
        when(notificacionRepository.save(any(Notificacion.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        String response = notificacionService.enviarNotificacionReserva(request);

        verify(notificacionRepository).save(any(Notificacion.class));
        assertThat(response).isEqualTo("Notificación de reserva enviada");
    }

    @Test
    void obtenerNotificacionesDebeRetornarListadoMapeado() {
        Notificacion notificacion = new Notificacion();
        notificacion.setId(faker.number().randomNumber());
        notificacion.setDestinatario(faker.internet().emailAddress());
        notificacion.setMensaje(faker.lorem().sentence());
        notificacion.setFechaEnvio(LocalDateTime.now());

        when(notificacionRepository.findAll()).thenReturn(List.of(notificacion));

        List<NotificacionResponse> response = notificacionService.obtenerNotificaciones();

        assertThat(response).hasSize(1);
        assertThat(response.get(0).getId()).isEqualTo(notificacion.getId());
        assertThat(response.get(0).getDestinatario()).isEqualTo(notificacion.getDestinatario());
        assertThat(response.get(0).getMensaje()).isEqualTo(notificacion.getMensaje());
        assertThat(response.get(0).getFechaEnvio()).isEqualTo(notificacion.getFechaEnvio());
    }
}
