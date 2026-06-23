package com.classmate.usuarioservice.service;

import com.classmate.usuarioservice.exception.ResourceNotFoundException;
import com.classmate.usuarioservice.dto.UsuarioRequest;
import com.classmate.usuarioservice.dto.UsuarioResponse;
import com.classmate.usuarioservice.entity.Usuario;
import com.classmate.usuarioservice.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public UsuarioResponse crear(UsuarioRequest request) {

        log.info("Creando usuario con email {}",
                request.getEmail());

        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            log.error("El correo {} ya existe",
                    request.getEmail());
            throw new RuntimeException("El correo ya existe");
        }

        if (usuarioRepository.findByAuthUserId(request.getAuthUserId()).isPresent()) {
            log.error("El authUserId {} ya está asociado",
                    request.getAuthUserId());
            throw new RuntimeException("El authUserId ya está asociado a un usuario");
        }

        Usuario usuario = new Usuario();
        usuario.setAuthUserId(request.getAuthUserId());
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setEmail(request.getEmail());
        usuario.setTelefono(request.getTelefono());
        usuario.setFechaNacimiento(request.getFechaNacimiento());
        usuario.setTipoUsuario(request.getTipoUsuario());

        Usuario guardado = usuarioRepository.save(usuario);

        log.info("Usuario creado correctamente con id {}",
                guardado.getId());

        return toResponse(guardado);
    }

    public List<UsuarioResponse> listar() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public UsuarioResponse buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        return toResponse(usuario);
    }

    public UsuarioResponse buscarPorAuthUserId(Long authUserId) {
        Usuario usuario = usuarioRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        return toResponse(usuario);
    }

    public UsuarioResponse actualizar(Long id, UsuarioRequest request) {

        log.info("Actualizando usuario con id {}", id);

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        usuario.setAuthUserId(request.getAuthUserId());
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setEmail(request.getEmail());
        usuario.setTelefono(request.getTelefono());
        usuario.setFechaNacimiento(request.getFechaNacimiento());
        usuario.setTipoUsuario(request.getTipoUsuario());

        Usuario actualizado = usuarioRepository.save(usuario);

        log.info("Usuario {} actualizado correctamente",
                actualizado.getId());

        return toResponse(actualizado);
    }

    public void eliminar(Long id) {

        log.info("Eliminando usuario con id {}", id);

        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado");
        }

        usuarioRepository.deleteById(id);

        log.info("Usuario {} eliminado correctamente", id);
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getAuthUserId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getTelefono(),
                usuario.getFechaNacimiento(),
                usuario.getTipoUsuario()
        );
    }
}