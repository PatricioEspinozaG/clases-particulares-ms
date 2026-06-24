package com.classmate.notificacionservice;

import com.classmate.notificacionservice.entity.Notificacion;
import com.classmate.notificacionservice.repository.NotificacionRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Locale;

@Profile({"dev","docker"})
@Component
public class DataLoader implements CommandLineRunner {

    private final NotificacionRepository notificacionRepository;

    public DataLoader(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    @Override
    public void run(String... args) {
        if (notificacionRepository.count() > 0) {
            return;
        }

        Faker faker = new Faker(new Locale("es"));

        for (int i = 0; i < 20; i++) {
            Notificacion notificacion = new Notificacion();
            notificacion.setDestinatario(faker.internet().emailAddress());
            notificacion.setMensaje(faker.options().option(
                    "Tu reserva fue creada correctamente.",
                    "Tu pago fue registrado correctamente.",
                    "La clase fue confirmada por el profesor.",
                    "Tu reserva fue actualizada.",
                    "Recuerda revisar el estado de tu próxima clase."
            ));
            notificacion.setFechaEnvio(LocalDateTime.now().minusHours(faker.number().numberBetween(0, 72)));

            notificacionRepository.save(notificacion);
        }
    }
}
