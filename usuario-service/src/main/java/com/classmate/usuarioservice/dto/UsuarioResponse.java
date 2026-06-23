package com.classmate.usuarioservice.dto;

import com.classmate.usuarioservice.entity.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@Data
@AllArgsConstructor
// RepresentationModel permite agregar links HATEOAS a la respuesta
// Ej: { "id": 1, "nombre": "Juan", "_links": { "self": { "href": "..." } } }
public class UsuarioResponse extends RepresentationModel<UsuarioResponse> {

    private Long id;
    private Long authUserId;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private LocalDate fechaNacimiento;
    private TipoUsuario tipoUsuario;
}
