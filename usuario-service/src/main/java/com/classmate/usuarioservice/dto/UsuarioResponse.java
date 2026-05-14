package com.classmate.usuarioservice.dto;

import com.classmate.usuarioservice.entity.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UsuarioResponse {

    private Long id;
    private Long authUserId;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private LocalDate fechaNacimiento;
    private TipoUsuario tipoUsuario;
}