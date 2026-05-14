CREATE TABLE clases (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    asignatura VARCHAR(255) NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    precio DOUBLE NOT NULL,
    fecha DATETIME NOT NULL,
    duracion INT NOT NULL,
    profesor_id BIGINT NOT NULL
);