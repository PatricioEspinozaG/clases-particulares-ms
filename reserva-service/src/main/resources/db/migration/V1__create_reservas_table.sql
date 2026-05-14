CREATE TABLE reservas (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          usuario_id BIGINT NOT NULL,
                          profesor_id BIGINT NOT NULL,
                          clase_id BIGINT NOT NULL,
                          fecha_reserva DATETIME NOT NULL,
                          estado VARCHAR(50) NOT NULL
);