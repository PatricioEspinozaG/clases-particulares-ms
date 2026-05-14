package com.classmate.usuarioservice.service;

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

        Usuario usuario = new Usuario();
        usuario.setAuthUserId(request.getAuthUserId());
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setEmail(request.getEmail());
        usuario.setTelefono(request.getTelefono());

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
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return toResponse(usuario);
    }

    public UsuarioResponse actualizar(Long id, UsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setAuthUserId(request.getAuthUserId());
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setEmail(request.getEmail());
        usuario.setTelefono(request.getTelefono());

        return toResponse(usuarioRepository.save(usuario));
    }

    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
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
                usuario.getTelefono()
        );
    }
}