package com.classmate.pagoservice;

import com.classmate.pagoservice.entity.EstadoPago;
import com.classmate.pagoservice.entity.Pago;
import com.classmate.pagoservice.repository.PagoRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Locale;

@Profile({"dev", "docker"})
@Component
public class DataLoader implements CommandLineRunner {

    private final PagoRepository pagoRepository;

    public DataLoader(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    @Override
    public void run(String... args) {
        if (pagoRepository.count() > 0) {
            return;
        }

        Faker faker = new Faker(new Locale("es"));

        for (int i = 0; i < 20; i++) {
            Pago pago = new Pago();
            pago.setReservaId((long) faker.number().numberBetween(1, 21));
            pago.setMonto((double) faker.number().numberBetween(8000, 30000));
            pago.setMetodoPago(faker.options().option("TARJETA", "TRANSFERENCIA", "EFECTIVO"));
            pago.setEstado(faker.options().option(EstadoPago.PENDIENTE, EstadoPago.APROBADO, EstadoPago.RECHAZADO));
            pago.setFechaPago(LocalDateTime.now().minusDays(faker.number().numberBetween(0, 10)));

            pagoRepository.save(pago);
        }
    }
}
