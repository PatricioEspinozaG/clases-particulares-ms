CREATE TABLE profesores (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            usuario_id BIGINT NOT NULL UNIQUE,
                            especialidad VARCHAR(150) NOT NULL,
                            descripcion VARCHAR(500),
                            precio_hora DECIMAL(10,2) NOT NULL,
                            experiencia_anios INT,
                            estado VARCHAR(50) NOT NULL
);