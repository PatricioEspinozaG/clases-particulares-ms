package com.classmate.usuarioservice.service;
import com.classmate.usuarioservice.exception.ResourceNotFoundException;

import com.classmate.usuarioservice.dto.UsuarioRequest;
import com.classmate.usuarioservice.dto.UsuarioResponse;
import com.classmate.usuarioservice.entity.Usuario;
import com.classmate.usuarioservice.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public UsuarioResponse crear(UsuarioRequest request) {

        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("El correo ya existe");
        }

        if (usuarioRepository.findByAuthUserId(request.getAuthUserId()).isPresent()) {
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

        return toResponse(actualizado);
    }

    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado");
        }

        usuarioRepository.deleteById(id);
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