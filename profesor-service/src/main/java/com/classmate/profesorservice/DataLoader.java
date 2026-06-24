package com.classmate.profesorservice;

import com.classmate.profesorservice.entity.EstadoProfesor;
import com.classmate.profesorservice.entity.Profesor;
import com.classmate.profesorservice.repository.ProfesorRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Locale;


@Profile({"dev", "docker"})
@Component
public class DataLoader implements CommandLineRunner {

    private final ProfesorRepository profesorRepository;

    public DataLoader(ProfesorRepository profesorRepository) {
        this.profesorRepository = profesorRepository;
    }

    @Override
    public void run(String... args) {

        // Si ya hay datos, no vuelve a cargar
        if (profesorRepository.count() > 0) {
            return;
        }

        Faker faker = new Faker(new Locale("es"));

        // Lista de especialidades educativas realistas
        String[] especialidades = {
                "Matemáticas", "Física", "Química", "Biología",
                "Historia", "Inglés", "Programación", "Música",
                "Arte", "Filosofía"
        };

        for (int i = 0; i < 20; i++) {

            Profesor profesor = new Profesor();

            // Cada profesor se asocia a un usuarioId distinto (1 al 20)
            profesor.setUsuarioId((long) (i + 1));
            profesor.setEspecialidad(
                    faker.options().option(especialidades)
            );
            profesor.setDescripcion(faker.lorem().sentence(10));
            profesor.setPrecioHora(
                    BigDecimal.valueOf(
                            faker.number().numberBetween(8000, 35000)
                    )
            );
            profesor.setExperienciaAnios(
                    faker.number().numberBetween(1, 20)
            );
            profesor.setEstado(
                    faker.options().option(EstadoProfesor.ACTIVO, EstadoProfesor.INACTIVO)
            );

            profesorRepository.save(profesor);
        }
    }
}
