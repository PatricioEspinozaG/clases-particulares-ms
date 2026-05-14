CREATE TABLE pagos (

    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    reserva_id BIGINT NOT NULL,
    monto DOUBLE NOT NULL,
    metodo_pago VARCHAR(255) NOT NULL,
    estado VARCHAR(50) NOT NULL,
    fecha_pago DATETIME NOT NULL
);