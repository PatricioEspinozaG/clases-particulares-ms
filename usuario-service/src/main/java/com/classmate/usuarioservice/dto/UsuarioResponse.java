package com.classmate.usuarioservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioResponse {

    private Long id;
    private Long authUserId;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
}