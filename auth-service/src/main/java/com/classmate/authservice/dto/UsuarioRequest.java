package com.classmate.authservice.dto;

import com.classmate.authservice.entity.TipoUsuario;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UsuarioRequest {

    private Long authUserId;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private LocalDate fechaNacimiento;
    private TipoUsuario tipoUsuario;
}