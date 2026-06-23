package com.classmate.claseservice;

import com.classmate.claseservice.entity.Clase;
import com.classmate.claseservice.repository.ClaseRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Locale;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    private final ClaseRepository claseRepository;

    public DataLoader(ClaseRepository claseRepository) {
        this.claseRepository = claseRepository;
    }

    @Override
    public void run(String... args) {
        if (claseRepository.count() > 0) {
            return;
        }

        Faker faker = new Faker(new Locale("es"));

        for (int i = 0; i < 20; i++) {
            Clase clase = new Clase();
            clase.setAsignatura(faker.educator().course());
            clase.setDescripcion(faker.lorem().sentence(12));
            clase.setPrecio((double) faker.number().numberBetween(8000, 30000));
            clase.setFecha(LocalDateTime.now().plusDays(faker.number().numberBetween(1, 30)));
            clase.setDuracion(faker.options().option(45, 60, 90, 120));
            clase.setProfesorId((long) faker.number().numberBetween(1, 11));

            claseRepository.save(clase);
        }
    }
}
