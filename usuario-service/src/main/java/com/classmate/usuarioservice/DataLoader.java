package com.classmate.usuarioservice;

import com.classmate.usuarioservice.entity.TipoUsuario;
import com.classmate.usuarioservice.entity.Usuario;
import com.classmate.usuarioservice.repository.UsuarioRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Locale;


@Profile({"dev", "docker"})
@Component
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;

    public DataLoader(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(String... args) {

        // Si ya hay datos, no vuelve a cargar (idempotente)
        if (usuarioRepository.count() > 0) {
            return;
        }

        // Faker con locale español para nombres más realistas
        Faker faker = new Faker(new Locale("es"));

        for (int i = 0; i < 20; i++) {

            Usuario usuario = new Usuario();

            // DataFaker genera datos realistas automáticamente
            usuario.setAuthUserId((long) (i + 1));
            usuario.setNombre(faker.name().firstName());
            usuario.setApellido(faker.name().lastName());
            usuario.setEmail(faker.internet().emailAddress());
            usuario.setTelefono(faker.phoneNumber().cellPhone());
            usuario.setFechaNacimiento(
                    LocalDate.now().minusYears(
                            faker.number().numberBetween(18, 50)
                    )
            );
            // Asigna tipo aleatorio entre ESTUDIANTE y PROFESOR
            usuario.setTipoUsuario(
                    faker.options().option(TipoUsuario.ESTUDIANTE, TipoUsuario.PROFESOR)
            );

            usuarioRepository.save(usuario);
        }
    }
}
