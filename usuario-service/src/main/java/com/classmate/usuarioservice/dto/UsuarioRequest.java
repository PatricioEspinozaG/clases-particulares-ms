package com.classmate.usuarioservice.dto;

import com.classmate.usuarioservice.entity.TipoUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UsuarioRequest {

    @NotNull
    private Long authUserId;

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String telefono;

    @NotNull
    private LocalDate fechaNacimiento;

    @NotNull
    private TipoUsuario tipoUsuario;
}