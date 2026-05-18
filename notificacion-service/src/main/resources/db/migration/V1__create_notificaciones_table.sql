CREATE TABLE notificaciones (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    destinatario VARCHAR(255) NOT NULL,
    mensaje TEXT NOT NULL,
    fecha_envio DATETIME NOT NULL

);